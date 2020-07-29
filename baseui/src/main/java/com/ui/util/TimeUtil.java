package com.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static final String FORMAT_YYYY = "yyyy";

    public static final String FORMAT_YYYYMM = "yyyy-MM";
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD_ = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDDS = "yyyy年MM月dd日";
    public static final String FORMAT_YYYYMMS = "yyyy年MM月";

    public static final String FORMAT_YYYYM = "yyyy-M";

    public static final String FORMAT_YYYYMM01 = "yyyy-MM-01";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_HHMM = "HH:mm";
    public static final String FORMAT_MM_DD_HHMM = "MM/dd HH:mm";


    public static String format(long date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(date));
    }


    public static String format(String dateStr, String sourceFormat, String formatStr) {

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            SimpleDateFormat tmp = new SimpleDateFormat(sourceFormat);

            return format.format(tmp.parse(dateStr));
        } catch (ParseException e) {
            return dateStr;
        }
    }

    public static long parse(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
