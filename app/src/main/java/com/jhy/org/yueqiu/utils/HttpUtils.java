package com.jhy.org.yueqiu.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;
import java.util.Map.Entry;

public final class HttpUtils {
    private static OkHttpClient httpClient = new OkHttpClient();
    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logx logx = new Logx(HttpUtils.class);

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
        logx.e("发送GET请求: " + url);
        get(url, callback);
    }

    public static void post (String url, Map<String, String> params, Callback callback) {
        boolean isFirst = true;

        String json = "{";
        for (Entry<String, String> pair : params.entrySet()) {
            json += (isFirst ? "\"" : ",\"") + pair.getKey() + "\":\"" + pair.getValue() + "\"";
            isFirst = false;
        }
        json += "}";
        logx.e("post: " + json);

        post(url, json, callback);
    }
    public static void post (String url, String params, Callback callback) {
        RequestBody requestBody = RequestBody.create(TYPE_JSON, params);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        httpClient.newCall(request).enqueue(callback);
    }
    public static void post (String url, String params) {
    }
}