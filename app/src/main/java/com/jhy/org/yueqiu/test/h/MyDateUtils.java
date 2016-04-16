package com.jhy.org.yueqiu.test.h;

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

    public static Date getDate () {
        return new Date();
    }

    public static Date getDate (String text) {
        return getDate(text, DEFAULT_DATE_PATTERN);
    }

    public static Date getDate (String text, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
