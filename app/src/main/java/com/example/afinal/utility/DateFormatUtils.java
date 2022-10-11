package com.example.afinal.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    //time stamp to string format
    public static String longToStr(long timestamp, boolean isPreciseTime) {
        return longToStr(timestamp, getFormatPattern(isPreciseTime));
    }

    private static String longToStr(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(timestamp));
    }
    public static long strToLong(String dateStr, boolean isPreciseTime) {
        return strToLong(dateStr, getFormatPattern(isPreciseTime));
    }

    private static long strToLong(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getFormatPattern(boolean showSpecificTime) {
        if (showSpecificTime) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_YMD;
        }
    }
}
