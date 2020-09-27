package com.song.sakura.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.song.sakura.util.UserCache;
import com.ui.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonInterceptor implements Interceptor {


    private static String KEY = "sign";
    public static String Token = "authToken";


    private CommonParameter filter = new CommonParameter();


    public CommonInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl httpUrl = oldRequest.url();
        LogUtil.print(httpUrl.url());
        filter.deviceId = filter.getDeviceId();
        filter.ts = String.valueOf(System.currentTimeMillis());
//        filter.authToken = UserCache.INSTANCE.getAuthToken();
        if (filter.authToken == null) {
            filter.authToken = "";
        }
//        if (UserCache.INSTANCE.getUser() != null)
//            filter.userId = "" + UserCache.INSTANCE.getUser().getAccountId();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = new HttpUrl.Builder()
                .encodedPath(httpUrl.encodedPath())
                .scheme(oldRequest.url().scheme())
                .port(oldRequest.url().port())
                .host(oldRequest.url().host());

        Map<String, String> map = toSign(filter);
        for (String s : httpUrl.queryParameterNames()) {
            map.put(s, httpUrl.queryParameter(s));
        }
        Request newRequest = oldRequest;

        String body = "";
        if (oldRequest.body() != null && oldRequest.body().contentLength() > 0) {
            okio.Buffer buffer = new okio.Buffer();
            oldRequest.body().writeTo(buffer);
            body = buffer.readUtf8();
        }
        String sign = toSign(map, body);
        map.put("sign", sign);
        map.put(Token, filter.authToken);
        for (String key : map.keySet()) {
            authorizedUrlBuilder.addQueryParameter(key, map.get(key));
            //authorizedUrlBuilder.addQueryParameter(key, URLEncoder.encode(map.get(key), "UTF-8"));
        }
        String token = isBlank(UserCache.INSTANCE.getAuthToken()) ? "" : UserCache.INSTANCE.getAuthToken();
        // 新的请求
        if ("GET".equals(oldRequest.method())) {
            newRequest = oldRequest.newBuilder().get()
                    .addHeader("Authorization", "bearer " + token)
                    .url(authorizedUrlBuilder.build())
                    .build();
        } else if ("POST".equals(oldRequest.method())) {
            newRequest = oldRequest.newBuilder()
                    .addHeader("Authorization", "bearer " + token)
                    .method(oldRequest.method(), new FormBody.Builder().add("data", body).build())
                    .url(authorizedUrlBuilder.build())
                    .build();
        }
        return chain.proceed(newRequest);

    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String toSign(Map<String, String> paraPublic, Object obj) {
        HashMap<String, Object> properties = new HashMap<>();

        if (paraPublic != null && paraPublic.size() > 0) {
            for (String key : paraPublic.keySet()) {
                if (TextUtils.isEmpty(key)) continue;
                properties.remove(key);
                properties.put(key, "" + paraPublic.get(key));
            }
        }

        //过滤掉KEY
        Set<String> propertiesKeySets = properties.keySet();
        TreeSet<String> signParamNames = new TreeSet<String>();
        for (Object propertiesKeySet : propertiesKeySets) {
            String propertyKey = propertiesKeySet.toString();
            if (KEY.equals(propertyKey) || Token.equals(propertyKey)) {
                continue;
            }
            signParamNames.add(propertyKey);
        }
        //添加KEY
        List<String> keyValuePair = new ArrayList<>();
        for (String signParamName : signParamNames) {
            Object signParamValue = properties.get(signParamName);
            try {
                keyValuePair.add(signParamName + "=" + URLEncoder.encode(signParamValue.toString(), "UTF-8"));
            } catch (Exception e) {
                keyValuePair.add(signParamName + "=");
            }
        }
        String sign = join(keyValuePair, "&");
        String requestBodyJson = "";


        if (obj != null && !TextUtils.isEmpty(obj.toString())) {
            requestBodyJson = (obj instanceof String) ? obj.toString() : new Gson().toJson(obj.toString());
        }

        sign = sign + properties.get(Token) + requestBodyJson;
        LogUtil.print("LoggingInterceptor: sign -> " + sign);
        return toMD5(sign);
    }

    private static Map<String, String> toSign(Object object) {
        Class<?> aClass = object.getClass();
        Map<String, String> properties = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj1.compareTo(obj2);
            }
        });

        Field[] declaredFields = aClass.getDeclaredFields();
        if (declaredFields != null && declaredFields.length != 0) {
            try {
                for (Field declaredField : declaredFields) {

                    String name = declaredField.getName();
                    if (name.indexOf("$") > -1) {
                        continue;
                    }
                    Object value;
                    String stringLetter = name.substring(0, 1).toUpperCase();
                    String getName = "get" + (stringLetter + name.substring(1));
                    try {
                        Method getMethod = aClass.getMethod(getName, new Class[]{});
                        value = getMethod.invoke(object, new Object[]{});
                    } catch (Exception e) {
                        declaredField.setAccessible(true);
                        value = declaredField.get(object);
                    }

                    properties.put(name, value.toString());

                }
                return properties;
            } catch (Exception e) {
                return properties;
            }
        }
        return properties;
    }

    private static String join(final Iterable<?> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    private static String join(final Iterator<?> iterator, final String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            @SuppressWarnings("deprecation")
            // ObjectUtils.toString(Object) has been deprecated in 3.2
            final String result = objectToString(first);
            return result;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    private static String objectToString(final Object obj) {
        return obj == null ? "" : obj.toString();
    }


    private static String toMD5(String inStr) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // Log.e("555","result: " + buf.toString());//32位的加密
            // Log.e("555","result: " + buf.toString().substring(8,24));//16位的加密

        } catch (NoSuchAlgorithmException e) {
        }
        return buf.toString();
    }

}