package com.jhy.org.yueqiu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jhy.org.yueqiu.config.App;

import java.util.Set;

public class Preferences {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREFERENCES_MAIN = "yueqiu.main";
    private static Preferences singleton = null;

    protected Preferences (String prefName) {
        pref = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static Preferences getInstance () {
        if (singleton == null) {
            singleton = new Preferences(PREFERENCES_MAIN);
        }
        return singleton;
    }

    public boolean contains (String key) { return pref.contains(key); }

    public boolean get (String key, boolean def) { return pref.getBoolean(key, def); }
    public int get (String key, int def) { return pref.getInt(key, def); }
    public long get (String key, long def) { return pref.getLong(key, def); }
    public float get (String key, float def) { return pref.getFloat(key, def); }
    public Set<String> get (String key, Set<String> def) { return pref.getStringSet(key, def); }
    public String get (String key) { return get(key, ""); }
    public String get (String key, String def) { return pref.getString(key, def == null ? "" : def); }

    public Preferences set (String key, boolean value) { getEditor().putBoolean(key, value); return this; }
    public Preferences set (String key, int value) { getEditor().putInt(key, value); return this; }
    public Preferences set (String key, long value) { getEditor().putLong(key, value); return this; }
    public Preferences set (String key, float value) { getEditor().putFloat(key, value); return this; }
    public Preferences set (String key, String value) { getEditor().putString(key, value == null ? "" : value); return this;  }
    public Preferences set (String key, Set<String> value) { getEditor().putStringSet(key, value); return this; }

    public Preferences remove (String key) { editor.remove(key); return this; }
    public Preferences clear () { editor.clear(); return this; }

    public Preferences commit () {
//        if (editor != null) {
//            editor.commit();
//            editor = null;
//        }
        editor.commit();
        return this;
    }

    private SharedPreferences.Editor getEditor () {
        if (editor == null) {
            editor = pref.edit();
        }
        return editor;
    }
}
