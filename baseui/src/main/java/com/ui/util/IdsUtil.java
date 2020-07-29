package com.ui.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdsUtil {
    public static String toString(List<String> list) {
        return toString(list, ",");
    }

    public static String toString(List<String> list, String s) {
        if (TextUtils.isEmpty(s))
            s = ",";
        String str = "";
        if (list == null || list.size() == 0) {
            return str;
        }
        boolean isImages = false;
        for (String img : list) {
            str += img + s;
            isImages = true;
        }
        if (isImages) {
            str = str.substring(0, str.length() - s.length());
        }
        return str;
    }

    public static String toInteger(List<Integer> list, String s) {
        if (s == null)
            s = "";
        String str = "";
        if (list == null || list.size() == 0) {
            return str;
        }
        boolean isImages = false;
        for (Integer img : list) {
            str += String.valueOf(img) + s;
            isImages = true;
        }
        if (isImages && s.length() > 0) {
            str = str.substring(0, str.length() - s.length());
        }
        return str;
    }

    public static String toInteger(Integer[] list, String s) {
        if (s == null)
            s = "";
        String str = "";
        if (list == null || list.length == 0) {
            return str;
        }
        boolean isImages = false;
        for (Integer img : list) {
            str += String.valueOf(img) + s;
            isImages = true;
        }
        if (isImages && s.length() > 0) {
            str = str.substring(0, str.length() - s.length());
        }
        return str;
    }

    public static String toInteger(int[] list, String s) {
        if (s == null)
            s = "";
        String str = "";
        if (list == null || list.length == 0) {
            return str;
        }
        boolean isImages = false;
        for (int img : list) {
            str += String.valueOf(img) + s;
            isImages = true;
        }
        if (isImages && s.length() > 0) {
            str = str.substring(0, str.length() - s.length());
        }
        return str;
    }

    public static List<String> getList(String src) {
        return getList(src, ",", true);
    }

    public static List<String> getList(String src, String split, boolean isRepeat) {
        if (src == null || "".equals(src)) {
            return Lists.newArrayList();
        }
        if (TextUtils.isEmpty(split)) {
            split = ",";
        }
        List<String> list = (List<String>) Arrays.asList(src.split(split));
        if (list == null) return Lists.newArrayList();
        if (!isRepeat) {
            return new ArrayList<String>(list);
        }
        List<String> listnew = Lists.newArrayList();
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                boolean isAdd = false;
                for (String strnew : listnew) {
                    if (str.equals(strnew)) {
                        isAdd = true;
                        break;
                    }
                }
                if (!isAdd) {
                    listnew.add(str);
                }
            }
        }
        return listnew;
    }

    public static List<String> getTagList(String src, String split, boolean isRepeat, boolean isEmpty) {
        if (src == null || "".equals(src)) {
            return Lists.newArrayList();
        }
        if (TextUtils.isEmpty(split)) {
            split = ",";
        }
        List<String> list = (List<String>) Arrays.asList(src.split(split));
        if (list == null) return Lists.newArrayList();
        if (!isRepeat) {
            return list;
        }
        List<String> listnew = Lists.newArrayList();
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                boolean isAdd = false;
                for (String strnew : listnew) {
                    if (str.equals(strnew)) {
                        isAdd = true;
                        break;
                    }
                }
                if (!isAdd) {
                    if (isEmpty && !TextUtils.isEmpty(str))
                        listnew.add(str);
                }
            }
        }
        return listnew;
    }

    public static void toLog(List list) {
        if (list != null)
            for (Object o : list)
                LogUtil.print(o);

    }
}
