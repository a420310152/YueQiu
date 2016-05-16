package com.jhy.org.yueqiu.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.jhy.org.yueqiu.config.App;

import io.rong.imkit.utils.StringUtils;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public final class Logx {
    private static final String TAG = "(Logx)";
    private String className = "";

    public Logx (Class<?> cls) {
        className = "  [ " + cls.getSimpleName() + " ]  ";
    }

    public void e (String text) { Log.e(TAG, getMessage(text)); }
    public void w (String text) { Log.w(TAG, getMessage(text)); }
    public void i (String text) { Log.i(TAG, getMessage(text)); }
    public void d (String text) { Log.d(TAG, getMessage(text)); }
    public void v (String text) { Log.v(TAG, getMessage(text)); }

    public void toast (String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
        e(text);
    }
    public void toast (int resId) {
        Toast.makeText(App.getInstance(), resId, Toast.LENGTH_SHORT).show();
        e(App.getInstance().getResources().getText(resId).toString());
    }

    private String getMessage (String text) {
        return className + text;
    }
}
