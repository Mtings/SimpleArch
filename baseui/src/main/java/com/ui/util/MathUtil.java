package com.ui.util;

import java.math.BigDecimal;

public class MathUtil {
    /**
     * 比较大小
     * 第一个数大
     *
     * @param bBegin
     * @param bEnd
     * @return
     */
    public static boolean compareBegin(BigDecimal bBegin, BigDecimal bEnd) {
        if (bBegin == null)
            return false;
        if (bEnd == null)
            return true;
        return bBegin.compareTo(bEnd) > 0;
    }

    /**
     * 2个数相等
     *
     * @param bBegin
     * @param bEnd
     * @return
     */
    public static boolean compareEquals(BigDecimal bBegin, BigDecimal bEnd) {
        if (bBegin == null)
            return false;
        if (bEnd == null)
            return false;
        return bBegin.compareTo(bEnd) == 0;
    }

    public static boolean compareEquals(Float f1, Float f2) {
        if (f1 == null)
            return false;
        if (f2 == null)
            return false;
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(float f1, float f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(Double f1, Double f2) {
        if (f1 == null)
            return false;
        if (f2 == null)
            return false;
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(double f1, double f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(double f1, float f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(float f1, double f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(double f1, int f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }

    public static boolean compareEquals(double f1, long f2) {
        return compareEquals(new BigDecimal(f1), new BigDecimal(f2));
    }
}
