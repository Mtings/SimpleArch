package com.ui.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class UrlUtils {

    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }
        return strPage;
    }

    public static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("/");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }


    public static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     *
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            if (arrSplitEqual.length > 1) {
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static boolean isEndEquals(String s0, String s1) {
        if (TextUtils.isEmpty(s0)) return false;
        if (s0.length() == 1) return s0.equals(s1);
        return s0.substring(s0.length() - 1).equals(s1);
    }

    public static String addEndSeparator(String url) {
        if (isEndEquals(url, "/")) {
            return url;
        } else {
            if (TextUtils.isEmpty(url)) return url;
            return url + "/";
        }
    }

    public static String deleteBeginSeparator(String url) {
        if (isBeginEquals(url, "/")) {
            return url.substring(1);
        } else {
            return url;
        }
    }

    public static boolean isBeginEquals(String s0, String s1) {
        if (TextUtils.isEmpty(s0)) return false;
        if (s0.length() == 1) return s0.equals(s1);
        return s0.substring(0, 1).equals(s1);
    }

    public static boolean isBeginIndexOf(String url, String index) {
        if (TextUtils.isEmpty(url)) return false;
        return url.indexOf(index) > -1;
    }
}
 