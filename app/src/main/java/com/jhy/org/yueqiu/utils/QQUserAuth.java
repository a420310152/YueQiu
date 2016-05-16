package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jhy.org.yueqiu.config.Key;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class QQUserAuth extends ThirdUserAuth implements IUiListener {
    private static final String SCOPE = "all";
    private static final int ON_COMPLETE = 0;
    private static final int ON_ERROR = 1;
    private static final int ON_CANCEL = 2;
    private static Logx logx = new Logx(QQUserAuth.class);

    private Tencent tencent = null;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ON_COMPLETE:
                    JSONObject response = (JSONObject)msg.obj;
                    try {
                        String userId = response.getString("openid");
                        String accessToken = response.getString("access_token");
                        long expiresIn = response.getLong("expires_in");

                        saveLoginInfo(userId, accessToken, expiresIn);
                        login(userId, accessToken, expiresIn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    logx.e("QQ回调 onComplete");
                    break;
                case ON_ERROR:
                    UiError e = (UiError) msg.obj;
                    logx.e("QQ回调 errorMsg:" + e.errorMessage + "errorDetail:" + e.errorDetail);
                    break;
                case ON_CANCEL:
                    logx.e("QQ回调 onCancel");
                    break;
            }
        }
    };

    private boolean isCaneled = false;

    public QQUserAuth (Activity activity) {
        super(activity, TYPE_QQ);
    }

    public Tencent getTencent () {
        if (tencent == null) {
            tencent = Tencent.createInstance(Key.qq.app_id, activity);
            String userId = preferences.get(PREFIX + snsType + KEY_USER_ID);
            String accessToken = preferences.get(PREFIX + snsType + KEY_ACCESS_TOKEN);
            long expiresIn = preferences.get(PREFIX + snsType + KEY_EXPIRES_IN, System.currentTimeMillis());

            expiresIn = (expiresIn - System.currentTimeMillis()) / 1000;
            if (!(Utils.isEmpty(userId) || Utils.isEmpty(accessToken))) {
                tencent.setOpenId(userId);
                tencent.setAccessToken(accessToken, "" + expiresIn);
            }
        }
        return tencent;
    }

    public QQUserAuth login () {
        getTencent().login(activity, SCOPE, this);
        return this;
    }

    public QQUserAuth onActivityResultData (int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
        return this;
    }

    public QQUserAuth cancel() {
        isCaneled = true;
        return this;
    }

    @Override
    public void onComplete(Object response) {
        if (isCaneled) return;
        Message msg = handler.obtainMessage();
        msg.what = ON_COMPLETE;
        msg.obj = response;
        handler.sendMessage(msg);
    }

    @Override
    public void onError(UiError e) {
        if (isCaneled) return;
        Message msg = handler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel() {
        if (isCaneled) return;
        Message msg = handler.obtainMessage();
        msg.what = ON_CANCEL;
        handler.sendMessage(msg);
    }
}
