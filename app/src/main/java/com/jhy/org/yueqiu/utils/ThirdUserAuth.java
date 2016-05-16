package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jhy.org.yueqiu.activity.MainActivity;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.Key;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class ThirdUserAuth {
    public static final String TYPE_QQ = "qq";
    public static final String TYPE_WEIBO = "weibo";
    public static final String TYPE_WEIXIN= "weixin";

    protected static final String PREFIX= "third_user.";
    protected static final String KEY_USER_ID = ".user_id";
    protected static final String KEY_ACCESS_TOKEN = ".access_token";
    protected static final String KEY_EXPIRES_IN = ".expires_in";

    private static Logx logx = new Logx(ThirdUserAuth.class);
    protected static Preferences preferences = Preferences.getInstance();

    protected Activity activity;
    protected String snsType = "";

    public ThirdUserAuth (Activity activity, String snsType) {
        this.activity = activity;
        this.snsType = snsType;
    }

    public void login (String userId, String accessToken, long expiresIn, OtherLoginListener listener) {
        BmobUser.BmobThirdUserAuth auth = new BmobUser.BmobThirdUserAuth(snsType, accessToken, "" + expiresIn, userId);
        BmobUser.loginWithAuthData(App.getInstance(), auth, listener);
    }
    public void login (String userId, String accessToken, long expiresIn) {
        login(userId, accessToken, expiresIn, new OtherLoginListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                logx.toast("登录成功！");
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("登录失败： i=" + i + ", s=" + s);
            }
        });
    }

    protected void saveLoginInfo (String userId, String accessToken, long expiresIn) {
        expiresIn = System.currentTimeMillis() + expiresIn * 1000;

        preferences.set(PREFIX + snsType + KEY_USER_ID, userId);
        preferences.set(PREFIX + snsType + KEY_ACCESS_TOKEN, accessToken);
        preferences.set(PREFIX + snsType + KEY_EXPIRES_IN, expiresIn);
        preferences.commit();
    }

    public void associate (String userId, String accessToken, String expiresIn, UpdateListener listener) {
        BmobUser.BmobThirdUserAuth auth = new BmobUser.BmobThirdUserAuth(snsType, accessToken, expiresIn, userId);
        BmobUser.associateWithAuthData(App.getInstance(), auth, listener);
    }

    public void dissociate (UpdateListener listener) {
        BmobUser.dissociateAuthData(App.getInstance(), snsType, listener);
    }
}
