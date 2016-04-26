package com.jhy.org.yueqiu.utils;

import java.util.Collection;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public final class Utils {

    public static boolean isEmpty (CharSequence str) {
        return str == null || str.length() == 0;
    }
    public static <T> boolean isEmpty (Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean equals (Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }
}
