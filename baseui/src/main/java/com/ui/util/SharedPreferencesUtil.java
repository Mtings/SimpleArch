package com.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static final String CONFIG_FILE = "Config_file";

    /**
     * 读取数据 SharedPreferences
     *
     * @param context
     * @param configname
     * @param name
     * @return
     */
    public static String get(Context context, String configname, String name) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(configname, 0);
            String check = userInfo.getString(name, "");
            return check;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 保存数据到 SharedPreferences
     *
     * @param context
     * @param configname
     * @param name
     * @param data
     */
    public static void set(Context context, String configname, String name, String data) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(configname, 0);
            userInfo.edit().putString(name, data).commit();
        } catch (Exception e) {
        }
    }

    /**
     * 读取数据 SharedPreferences
     *
     * @param context
     * @param configname
     * @param name
     * @return
     */
    public static boolean getBoolean(Context context, String configname, String name, boolean value) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(configname, 0);
            boolean check = userInfo.getBoolean(name, value);
            return check;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 保存数据到 SharedPreferences
     *
     * @param context
     * @param configname
     * @param name
     * @param data
     */
    public static void setBoolean(Context context, String configname, String name, boolean data) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(configname, 0);
            userInfo.edit().putBoolean(name, data).commit();
        } catch (Exception e) {
        }
    }
}
