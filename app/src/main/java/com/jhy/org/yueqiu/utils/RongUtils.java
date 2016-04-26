package com.jhy.org.yueqiu.utils;

import android.util.ArrayMap;

import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.domain.Person;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public final class RongUtils {
    private static boolean isConnected = false;
    private static final String TOKEN = "user.token";
    private static Logx logx = new Logx(RongUtils.class);

    public static void connect () {
        App app = App.getInstance();
        String token = Preferences.get(TOKEN);
        if (app != null && !isConnected && !token.equals("")) {
            String curPorcessName = App.getCurProcessName(app);
            String packageName = app.getApplicationInfo().packageName;
            if (packageName.equals(curPorcessName)) {
                RongIM.connect(token, connectCallback);
            }
        }
    }

    public static void requestToken (String userId, String name, String portraitUri) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        BmobUtils.post(BmobUtils.FUNC_GET_TOKEN, params, requestCallback);
    }
    public static void requestToken () {
        String userId = Preferences.get("user.id");
        String name = Preferences.get("user.name");
        String portaitUri = Preferences.get("portraitUri");
        if (userId.equals("") || name.equals("") || portaitUri.equals("")) {
            BmobUser currentUser = BmobUser.getCurrentUser(App.getInstance());
        }
    }

    private static RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {
            isConnected = false;
            logx.e("connect 失败: token不正确!");
        }

        @Override
        public void onSuccess(String s) {
            isConnected = true;
            logx.e("connect 成功!");
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            isConnected = true;
            logx.e("connect 失败: " + errorCode.toString());
        }
    };

    private static Callback requestCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            logx.e("请求TOKEN 失败: " + request.body().toString());
            logx.e("\t\t\t" + e.toString());
        }

        @Override
        public void onResponse(Response response) throws IOException {
            logx.e("请求TOKEN 成功: " + response.body().toString());
        }
    };
}
