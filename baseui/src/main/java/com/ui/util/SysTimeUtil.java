package com.ui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class SysTimeUtil {
    private final static String CONFIG_NAME = "SysTimeUtil";
    private final static String CURRENT_TIME_NAME = "CURRENT_TIME_NAME";
    private final static String ELAPSED_TIME_NAME = "ELAPSED_TIME_NAME";

    public static void initTime(Context context, long sysTime) {
        if (context != null&&sysTime>0) {
            saveConfig(context, CURRENT_TIME_NAME, "" + sysTime);
            saveConfig(context, ELAPSED_TIME_NAME, "" + SystemClock.elapsedRealtime());
        }
    }

    /**
     * 如果没有initTime的话 获取的时间将不准确
     *
     * @param context
     * @return
     */
    public static long getSysTime(Context context) {
        if (context != null) {
            long saveCurrentTimeMillis = 0;
            try {
                saveCurrentTimeMillis = Long.valueOf(getConfig(context, CURRENT_TIME_NAME));
            } catch (Exception e) {
            }
            long saveElapsedRealtime = 0;
            try {
                saveElapsedRealtime = Long.valueOf(getConfig(context, ELAPSED_TIME_NAME));
            } catch (Exception e) {
            }

            long sysTime = saveCurrentTimeMillis + (SystemClock.elapsedRealtime() - saveElapsedRealtime);
            if(sysTime<=0)
            {
                return System.currentTimeMillis();
            }
            return sysTime;
        }
        return 0l;
    }

    private static void saveConfig(Context context, String name, String data) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(CONFIG_NAME, 0);
            userInfo.edit().putString(name, data).commit();
        } catch (Exception e) {
        }
    }

    private static String getConfig(Context context, String name) {
        try {
            SharedPreferences userInfo = context.getSharedPreferences(CONFIG_NAME, 0);
            String check = userInfo.getString(name, "");
            return check;
        } catch (Exception e) {
        }
        return "";
    }
}
