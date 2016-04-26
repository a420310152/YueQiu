package com.jhy.org.yueqiu.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public final class Logx {
    private static final String HEADER = "(Logx)";
    private String className = "";

    public Logx (Class<?> cls) {
        className = "  ==[ " + cls.getSimpleName() + " ]==  ";
    }

    public void e (String text) {
        Log.e(HEADER, className + text);
    }

    public void w (String text) {
        Log.w(HEADER, className + text);
    }

    public void i (String text) {
        Log.i(HEADER, className + text);
    }
}
