package com.ui.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ScoreUtil {

    public static String formatScore(int score) {
        return getTencentInteger(toScoreFloat(score));
    }

    private static String getTencentInteger(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.0");
        ds = fmt.format(price);
        return ds;
    }

    private static BigDecimal toScoreFloat(int f) {
        BigDecimal d = new BigDecimal(f);
        d = d.divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP);
        return d;
    }
}
