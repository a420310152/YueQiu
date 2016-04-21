package com.jhy.org.yueqiu.test.h;

import com.jhy.org.yueqiu.config.MyApplication;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public final class RongUtils {
    public static void connect (String token) {
        MyApplication app = MyApplication.getInstance();
        if (app != null) {
            String curPorcessName = MyApplication.getCurProcessName(MyApplication.getInstance());
            if (curPorcessName.equals(app.getApplicationInfo().packageName)) {
                RongIM.connect(token, new RongIMClient.ConnectCallback () {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }

                    @Override
                    public void onTokenIncorrect() {

                    }
                });
            }
        }
    }
}
