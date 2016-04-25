package com.jhy.org.yueqiu.test.h.backups;

import android.util.Log;

import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.utils.Preferences;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public final class RongUtils {
    private static boolean isConnected = false;

    public static void connect () {
        App app = App.getInstance();
        String token = Preferences.get("token");
        if (app != null && !isConnected && !token.equals("")) {
            String curPorcessName = App.getCurProcessName(app);
            String packageName = app.getApplicationInfo().packageName;
            if (packageName.equals(curPorcessName)) {
                RongIM.connect(token, connectCallback);
            }
        }
    }

    private static RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {
            isConnected = false;
            Log.i("ilog", "connect 失败: token不正确!");
        }

        @Override
        public void onSuccess(String s) {
            isConnected = true;
            Log.i("ilog", "connect 成功");
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            isConnected = true;
            Log.i("ilog", "connect 失败: " + errorCode.toString());
        }
    };
}
