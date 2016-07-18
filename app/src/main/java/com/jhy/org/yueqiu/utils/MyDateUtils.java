package com.jhy.org.yueqiu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public final class MyDateUtils {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static Date toDate (String text, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static Date toDate (String text) {
        return toDate(text, DEFAULT_DATE_PATTERN);
    }
    public static Date toDate () {
        return new Date();
    }

    public static String toString (Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    public static String toString (Date date) {
        return toString(date, DEFAULT_DATE_PATTERN);
    }
    public String toString () {
        return toString(new Date());
    }
}
