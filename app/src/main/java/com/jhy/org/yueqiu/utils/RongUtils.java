package com.jhy.org.yueqiu.utils;

import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.domain.Person;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public final class RongUtils {
    private static boolean isConnected = false;
    private static boolean isRequestingToken = false;

    private static Logx logx = new Logx(RongUtils.class);

    public static void initialize (App app) {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        String curProcessName = app.getCurProcessName(app.getApplicationContext());
        String packageName = app.getApplicationInfo().packageName;
        if (packageName.equals(curProcessName) || "io.rong.push".equals(curProcessName)) {
            RongIM.init(app);
            connect();
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String userId) {

                    return null;
                }
            }, true);
        }
    }

    public static void connect () {
        App app = App.getInstance();
        String token = Preferences.get(App.user.token);

        if (app != null && !isConnected && checkToken()) {
            String curPorcessName = App.getCurProcessName(app);
            String packageName = app.getApplicationInfo().packageName;

            if (packageName.equals(curPorcessName)) {

                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        isConnected = false;
                        logx.e("connect 失败: token不正确!");
                        requestToken((String) BmobUser.getObjectByKey(App.getInstance(), "objectId"));
                    }
                    @Override
                    public void onSuccess(String s) {
                        isConnected = true;
                        logx.e("connect 成功:");
                        logx.e("\t\t\tuserId = " + s);
                        logx.e("\t\t\tusername = " + Preferences.get(App.user.name));
                        logx.e("\t\t\ttoken = " + Preferences.get(App.user.token));
                    }
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        isConnected = true;
                        logx.e("connect 失败: " + errorCode.toString());
                    }
                });
            }
        }
    }

    public static void requestToken (String userId, String name, String portraitUri) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);

        BmobUtils.post(BmobUtils.FUNC_GET_TOKEN, params, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                isRequestingToken = false;
                logx.e("请求TOKEN 失败: " + e.toString());
                logx.e("\t\t\t" + request.body().toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                isRequestingToken = false;
                String data = response.body().string();
                try {
                    JSONObject obj = new JSONObject(data);
                    if (obj != null) {
                        String token = obj.getString("token");
                        if (token != null) {
                            Preferences.set(App.user.token, token);
                            connect();
                        } else {
                            logx.e("请求TOKEN 成功, 但并没有获得TOKEN: " + data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void requestToken (String userId) {
        if (!Utils.isEmpty(userId)) {
            BmobQuery<Person> query = new BmobQuery<>();
            query.getObject(App.getInstance(), userId, new GetListener<Person>() {
                @Override
                public void onSuccess(Person person) {
                    logx.e("查询用户信息 成功!");
                    String userId = person.getObjectId();
                    String name = person.getUsername();
                    String portaitUri = person.getAvatarUrl();

                    Preferences.set(App.user.id, userId);
                    Preferences.set(App.user.name, name);
                    Preferences.set(App.user.portrait_uri, portaitUri);

                    requestToken(userId, name, portaitUri);
                }

                @Override
                public void onFailure(int i, String s) {
                    logx.e("查询用户信息 失败: " + s);
                }
            });
        }
    }
    public static void requestToken () {
        checkToken();
    }

    public static boolean checkToken () {
        String userId = Preferences.get(App.user.id);
        String name = Preferences.get(App.user.name);
        String portaitUri = Preferences.get(App.user.portrait_uri);
        String token = Preferences.get(App.user.token);

        Person person = BmobUser.getCurrentUser(App.getInstance(), Person.class);
        if (person != null) {
            boolean needsUpdate = !Utils.equals(userId, person.getObjectId())
                    || !Utils.equals(name, person.getUsername())
                    || Utils.isEmpty(token);
            if (needsUpdate && !isRequestingToken) {
                logx.e("checkToken 需要更新TOKEN");
                requestToken(person.getObjectId());
                isRequestingToken = true;
                return false;
            }
        }
        return true;
    }
}
