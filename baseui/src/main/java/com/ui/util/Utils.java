package com.ui.util;

import com.ui.R;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static android.content.Context.CLIPBOARD_SERVICE;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
public class Utils {

    public static final String DIR = "Ware";
    public static final String regularExpression = "[a-zA-Z0-9\\s]";
    public static final String regularExpressionPunctuation = "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×°]";

    public static String getDistance(double distance) {
        distance = distance / 1000d;
        DecimalFormat fmt = new DecimalFormat("0.##");
        String ds = fmt.format(distance) + "千米 ";
        return ds;
    }

    public static String getDistance(double lat, double lon, double lat1, double lon1) {

        String ds = "";
        double distance = distanceBetween(
                lat, lon,
                lat1, lon1);
        distance = Math.abs(distance);
        if (distance >= 0) {
            distance = distance / 1000d;
            DecimalFormat fmt = new DecimalFormat("0.##");
            ds = fmt.format(distance) + "千米 ";
        } else {
            DecimalFormat fmt = new DecimalFormat("0.##");
            ds = fmt.format((int) distance) + "米 ";
        }

        return ds;
    }

    public static String getDistance2(double lat, double lon, double lat1, double lon1) {

        String ds = "";
        double distance = distanceBetween(
                lat, lon,
                lat1, lon1);
        distance = Math.abs(distance);
        if (MathUtil.compareBegin(new BigDecimal(distance), new BigDecimal(1000d))) {
            distance = distance / 1000d;
            DecimalFormat fmt = new DecimalFormat("0.##");
            ds = fmt.format(distance) + "km ";
        } else {
            DecimalFormat fmt = new DecimalFormat("0.##");
            ds = fmt.format((int) distance) + "m ";
        }

        return ds;
    }


    public static double distanceBetween(double startLat, double startLon, double endLat, double endLon) {
//    	float[] results = new float[1];
//    	android.location.Location.distanceBetween(
//				lat,
//				lon,
//				lat1,
//				lon1, results);
//    	return results[0];
        //return DistanceUtil.getDistance(new LatLng(lat, lon), new LatLng(lat1, lon1));
        double er = 6378137d; // 6378700.0f;
        double radlat1 = Math.PI * startLat / 180.0d;
        double radlat2 = Math.PI * endLat / 180.0d;
        //now long.
        double radlong1 = Math.PI * startLon / 180.0d;
        double radlong2 = Math.PI * endLon / 180.0d;
        if (radlat1 < 0) radlat1 = Math.PI / 2 + Math.abs(radlat1);// south
        if (radlat1 > 0) radlat1 = Math.PI / 2 - Math.abs(radlat1);// north
        if (radlong1 < 0) radlong1 = Math.PI * 2 - Math.abs(radlong1);//west
        if (radlat2 < 0) radlat2 = Math.PI / 2 + Math.abs(radlat2);// south
        if (radlat2 > 0) radlat2 = Math.PI / 2 - Math.abs(radlat2);// north
        if (radlong2 < 0) radlong2 = Math.PI * 2 - Math.abs(radlong2);// west
        //spherical coordinates x=r*cos(ag)sin(at), y=r*sin(ag)*sin(at), z=r*cos(at)
        //zero ag is up so reverse lat
        double x1 = er * Math.cos(radlong1) * Math.sin(radlat1);
        double y1 = er * Math.sin(radlong1) * Math.sin(radlat1);
        double z1 = er * Math.cos(radlat1);
        double x2 = er * Math.cos(radlong2) * Math.sin(radlat2);
        double y2 = er * Math.sin(radlong2) * Math.sin(radlat2);
        double z2 = er * Math.cos(radlat2);
        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //side, side, side, law of cosines and arccos
        double theta = Math.acos((er * er + er * er - d * d) / (2 * er * er));
        double dist = theta * er;
        return dist;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //    public static int dip2px(float dipValue) {
//        final float scale = BaseApplication.getAppContext().getResources().getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
//
    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Long getLong(String s) {
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static boolean getBoolean(String s) {
        try {
            return Boolean.parseBoolean(s.trim());
        } catch (Exception e) {
            return false;
        }
    }

    public static Float getFloat(String s) {
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            return 0f;
        }
    }

    public static Integer getInteger(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer getInteger(float s) {
        try {
            BigDecimal bigDecimal = new BigDecimal(s);
            return bigDecimal.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getInteger(Integer s) {
        if (s == null) return 0;
        return s;
    }

    public static Double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0D;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);
//        ToastUtils.showLong(context, context.getResources().getString(R.string.text_copied));
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap stickerBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(stickerBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return stickerBitmap;
    }

    public static int getStatusBarHeight(Activity context) {
        if (Build.VERSION.SDK_INT >= 21) {
            int statusBarHeight = -1;
            //获取status_bar_height资源的ID
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
//            Rect frame = new Rect();
//            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            return statusBarHeight == 0 ? dip2px(context, 24) : statusBarHeight;
        } else
            return 0;
    }
}
