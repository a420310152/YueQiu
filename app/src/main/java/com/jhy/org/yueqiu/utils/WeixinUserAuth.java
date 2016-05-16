package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.Key;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class WeixinUserAuth extends ThirdUserAuth implements IWXAPIEventHandler {
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String URL_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    private static final String TYPE_ACCESS_TOKEN = "authorization_code";
    private static final String TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String SCOPE = "snsapi_userinfo";
    private static final String STATE = "chx_yueqiu";
    private static final Logx logx = new Logx(WeixinUserAuth.class);
    private static IWXAPI api = null;

    private SendAuth.Req req;
    private String state = null;

    public WeixinUserAuth (Activity activity) {
        super(activity, TYPE_WEIXIN);
        req = new SendAuth.Req();
    }

    public static IWXAPI getApi () {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(App.getInstance(), Key.weixin.app_id, true);
            api.registerApp(Key.weixin.app_id);;
        }
        return api;
    }

    public void handleIntent (Intent intent) {
        getApi().handleIntent(intent, this);
    }

    public WeixinUserAuth login () {
        req.scope = SCOPE;
        req.state = refreshState();
        getApi().sendReq(req);
        return this;
    }

    private String refreshState () {
        state = STATE;
        return state;
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                logx.e("请求 得到消息");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                logx.e("请求 显示消息");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                String code = resp.code;
                requestToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        logx.toast(result);
    }

    private void requestToken (String code) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", TYPE_ACCESS_TOKEN);
        map.put("appid", Key.weixin.app_id);
        map.put("secret", Key.weixin.app_secret);
        map.put("code", code);
        HttpUtils.get(URL_ACCESS_TOKEN, map, new Callback() {
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
                    String openId = resp.optString("openid");
                    String accessToken = resp.optString("access_token");
                    long expiresIn = resp.optLong("expires_in");
                    String refreshToken = resp.optString("refresh_token");
                    String scope = resp.optString("scope");
                    String unionid = resp.optString("unionid");

                    saveLoginInfo(openId, accessToken, expiresIn);
                    login(openId, accessToken, expiresIn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshToken (String refreshToken) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", TYPE_REFRESH_TOKEN);
        map.put("appid", Key.weixin.app_id);
        map.put("refresh_token", refreshToken);
        HttpUtils.get(URL_REFRESH_TOKEN, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                logx.e("刷新 token 失败: request=" + request.body().toString() + ", exception=" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                logx.e("刷新 token 成功");
                String body = response.body().string();
                try {
                    JSONObject resp = new JSONObject(body);
                    String openId = resp.optString("openid");
                    String accessToken = resp.optString("access_token");
                    long expiresIn = resp.optLong("expires_in");
                    String refreshToken = resp.optString("refresh_token");
                    String scope = resp.optString("scope");
                    //String unionid = resp.optString("unionid");

                    saveLoginInfo(openId, accessToken, expiresIn);
                    login(openId, accessToken, expiresIn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
