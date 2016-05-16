package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jhy.org.yueqiu.config.Key;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class WeiboUserAuth extends ThirdUserAuth implements WeiboAuthListener {
    private static final String URL_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";
    private static final String URL_ACCESS_TOKEN= "https://api.weibo.com/oauth2/access_token";
    private static final String URL_REDIRECT = "https://api.weibo.com/oauth2/default.html";
    private static final String TYPE_ACCESS_TOKEN = "authorization_code";
    private static final String SCOPE = "email,direct_messages_read,direct_messages_write";
    private static final String STATE = "chx_yueqiu";
    private static final Logx logx = new Logx(WeiboUserAuth.class);

    private SsoHandler ssoHandler;
    private AuthInfo authInfo;
    private String state;

    public WeiboUserAuth(Activity activity) {
        super(activity, TYPE_WEIBO);
        authInfo = new AuthInfo(activity, Key.weibo.app_key, URL_REDIRECT, SCOPE);
    }

    public WeiboUserAuth login () {
        getSsoHandler().authorize(this);
        return this;
    }

    public SsoHandler getSsoHandler () {
        if (ssoHandler == null) {
            ssoHandler = new SsoHandler(activity, authInfo);
        }
        return ssoHandler;
    }

    private String refreshState () {
        state = STATE;
        return state;
    }

    public WeiboUserAuth onActivityResultData (int requestCode, int resultCode, Intent data) {
        getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        return this;
    }

    public void requestCode () {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", Key.weibo.app_key);
        map.put("redirect_uri", URL_REDIRECT);
        map.put("state", refreshState());
        HttpUtils.post(URL_AUTHORIZE, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                logx.e("请求 code 失败: request=" + request.body().toString() + ", exception=" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                logx.e("请求 code 成功");
                String body = response.body().string();
                try {
                    JSONObject resp = new JSONObject(body);
                    String state = resp.optString("state");
                    String code = resp.optString("code");

                    if (Utils.equals(state, WeiboUserAuth.this.state)) {
                        requestToken(code);
                    } else {
                        logx.toast("错误，禁止授权！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestToken (String code) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", Key.weibo.app_key);
        map.put("client_secret", Key.weibo.app_secret);
        map.put("grant_type", TYPE_ACCESS_TOKEN);
        map.put("redirect_uri", URL_REDIRECT);
        map.put("code", code);
        HttpUtils.post(URL_ACCESS_TOKEN, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                logx.e("请求 token 失败: request=" + request.body().toString() + ", exception=" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                logx.e("请求 token 成功");
                String body = response.body().string();
                try {
                    JSONObject resp = new JSONObject(body);
                    String uid = resp.optString("uid");
                    String accessToken = resp.optString("access_token");
                    long expiresIn = resp.optLong("expires_in");

                    saveLoginInfo(uid, accessToken, expiresIn);
                    login(uid, accessToken, expiresIn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onComplete(Bundle values) {
        Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
        if (accessToken != null && accessToken.isSessionValid()) {
            String uid = accessToken.getUid();
            String token = accessToken.getToken();
            long expiresIn = accessToken.getExpiresTime();

            saveLoginInfo(uid, token, expiresIn);
            login(uid, token, expiresIn);
            logx.e("微博授权成功后返回的信息: token = " + token + ",expires =" + expiresIn + ",uid = " + uid);
        } else {
            String code = values.getString("code", "");
            logx.e("微博授权失败：code=" + code);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        logx.toast(e.getMessage());
    }

    @Override
    public void onCancel() {
        logx.toast("取消weibo授权");
    }
}
