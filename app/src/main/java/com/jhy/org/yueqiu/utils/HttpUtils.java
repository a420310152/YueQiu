package com.jhy.org.yueqiu.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.Map;
import java.util.Map.Entry;

public final class HttpUtils {
    private static OkHttpClient httpClient = new OkHttpClient();
    private static final String BASE_URL = "http://webim.demo.rong.io/";

    public static void get (String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }
    public static void get (String url, Map<String, String> params, Callback callback) {
        boolean isFirst = true;
        for (Entry<String, String> pair : params.entrySet()) {
            url += (isFirst ? "?" : "&") + pair.getKey() + "=" + pair.getValue();
            isFirst = false;
        }
        get(url, callback);
    }
}