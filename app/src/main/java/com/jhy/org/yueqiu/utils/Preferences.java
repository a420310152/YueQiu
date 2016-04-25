package com.jhy.org.yueqiu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jhy.org.yueqiu.config.App;

import java.util.Set;

public final class Preferences {
    private static Context context;
    private static final String PREFERENCES_NAME = "hello";
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void initialize (Context context) {
        Preferences.context = context;
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private static SharedPreferences getPreferences () {
        if (context == null) {
            initialize(App.getInstance());
        }
        return preferences;
    }

    private static SharedPreferences.Editor getEditor () {
        if (context == null) {
            initialize(App.getInstance());
        }
        return editor;
    }

    public static boolean get (String key, boolean def) { return getPreferences().getBoolean(key, def); }
    public static int get (String key, int def) { return getPreferences().getInt(key, def); }
    public static long get (String key, long def) { return getPreferences().getLong(key, def); }
    public static float get (String key, float def) { return getPreferences().getFloat(key, def); }
    public static Set<String> get (String key, Set<String> def) { return getPreferences().getStringSet(key, def); }
    public static String get (String key, String def) { return getPreferences().getString(key, def); }
    public static String get (String key) { return get(key, ""); }

    public static void set (String key, boolean value) { getEditor().putBoolean(key, value).commit(); }
    public static void set (String key, int value) { getEditor().putInt(key, value).commit(); }
    public static void set (String key, long value) { getEditor().putLong(key, value).commit(); }
    public static void set (String key, float value) { getEditor().putFloat(key, value).commit(); }
    public static void set (String key, String value) { getEditor().putString(key, value).commit(); }
    public static void set (String key, Set<String> value) { getEditor().putStringSet(key, value).commit(); }

    public static void remove (String key) { getEditor().remove(key).commit(); }
}
