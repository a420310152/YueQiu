package com.jhy.org.yueqiu.utils;

import com.jhy.org.yueqiu.config.Key;
import com.squareup.okhttp.Callback;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public final class BmobUtils {
    private static final String BASE_CLOUD_URL = "http://cloud.bmob.cn/" + Key.bmob.secret_key + "/";
    public static final String FUNC_GET_TOKEN = "getToken";

    public static void get (String func, Map<String, String> params, Callback callback) {
        HttpUtils.get(BASE_CLOUD_URL + func, params, callback);
    }
    public static void get (String func, Callback callback) {
        HttpUtils.get(BASE_CLOUD_URL + func, callback);
    }

    public static void post (String func, Map<String, String>params, Callback callback) {
        HttpUtils.post(BASE_CLOUD_URL + func, params, callback);
    }
}
