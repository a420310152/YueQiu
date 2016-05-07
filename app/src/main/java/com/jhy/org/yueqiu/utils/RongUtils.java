package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jhy.org.yueqiu.activity.ContactActivity;
import com.jhy.org.yueqiu.activity.OpponentActivity;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.rong.ContactNotificationMessageProvider;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.TextMessage;

public class RongUtils {
    private static boolean isConnected = false;
    private static boolean isRequestingToken = false;
    private static String token = null;
    private static HashMap<String, UserInfo> userInfoMap;
    private static UserInfoPreferences userInfoPreferences;
    private static Preferences preferences;

    private static Logx logx = new Logx(RongUtils.class);

    public static void initialize (App app) {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        userInfoPreferences = UserInfoPreferences.getInstance();
        preferences = Preferences.getInstance();

        String curProcessName = App.getCurProcessName(app.getApplicationContext());
        String packageName = app.getApplicationInfo().packageName;
        if (packageName.equals(curProcessName) || "io.rong.push".equals(curProcessName)) {
            RongIM.init(app);
            //RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
            setConversationListBehaviorListener();
            setUserInfoProvider();
            connect();

            userInfoMap = new HashMap<>();
        }
    }

    // 连接RongIM, 这是一个必须的动作
    public static void connect () {
        App app = App.getInstance();
        if (!isConnected && checkToken()) {
            logx.e("正在connect...");
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
                    public void onSuccess(String userId) {
                        onConnectSuccess(userId);
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

    private static void onConnectSuccess (String userId) {
        isConnected = true;
        RongIM rong = RongIM.getInstance();

        String name = preferences.get(App.user.name);
        String portaitUri = preferences.get(App.user.portrait_uri);
        UserInfo userInfo = new UserInfo(userId, name, Uri.parse(portaitUri));

        rong.setCurrentUserInfo(userInfo);
        rong.setMessageAttachedUserInfo(true);
        refreshUserInfo(userInfo);

        setSendMessageListener();
    }

    public static void disconnect () {
        RongIM rong = RongIM.getInstance();
        if (rong != null) {
//            rong.getRongIMClient().logout();
            rong.disconnect();
        }
        isConnected = false;
    }

    // 请求token
    public static void requestToken (String userId, String name, String portraitUri) {
        if (Utils.isEmpty(portraitUri)) {
            portraitUri = Person.URL_DEFAULT_AVATAR;
        }
        preferences
                .set(App.user.id, userId)
                .set(App.user.name, name)
                .set(App.user.portrait_uri, portraitUri)
                .commit();

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
                    token = obj.getString("token");
                    if (token != null) {
                        preferences.set(App.user.token, token).commit();
                        connect();
                        logx.e("请求TOKEN 成功");
                    } else {
                        logx.e("请求TOKEN 成功, 但并没有获得TOKEN: " + data);
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
//                    logx.e("查询用户信息 成功!");
                    String userId = person.getObjectId();
                    String name = person.getUsername();
                    String portaitUri = person.getAvatarUrl();

                    requestToken(userId, name, portaitUri);
                    refreshUserInfo(userId, name, portaitUri);
                }

                @Override
                public void onFailure(int i, String s) {
                    logx.e("查询用户信息 失败: " + s);
                }
            });
        }
    }

    // 检查现存的token是否可用
    //      若可用则返回true
    //      若过时则返回false, 并会请求新的token
    public static boolean checkToken () {
        String userId = preferences.get(App.user.id);
        String name = preferences.get(App.user.name);
        String portaitUri = preferences.get(App.user.portrait_uri);
        token = preferences.get(App.user.token);

        Person person = Person.getCurrentUser();
        if (person != null) {
            boolean needsUpdate = Utils.isEmpty(token)
                    || !Utils.equals(portaitUri, person.getAvatarUrl())
                    || !Utils.equals(userId, person.getObjectId())
                    || !Utils.equals(name, person.getUsername());
            if (needsUpdate && !isRequestingToken) {
//                logx.e("checkToken 需要更新TOKEN");
                requestToken(person.getObjectId());
                isRequestingToken = true;
                return false;
            }
        }
        return true;
    }


    public static void refreshUserInfo (UserInfo userInfo) {
        if (userInfo != null) {
            RongIM rong = RongIM.getInstance();
            if (rong != null) {
                rong.refreshUserInfoCache(userInfo);
            }
            userInfoMap.put(userInfo.getUserId(), userInfo);
            userInfoPreferences.setUserInfo(userInfo).commit();
        }
    }
    public static void refreshUserInfo (String userId, String name, String portraitUri) {
        refreshUserInfo(new UserInfo(userId, name, Uri.parse(portraitUri)));
    }
    public static void refreshUserInfo (Person person) {
        if (person != null) {
            refreshUserInfo(person.getObjectId(), person.getUsername(), person.getAvatarUrl());
        }
    }

    private static void refreshUserInfoFromCloud (String userId) {
        Person.query(userId, new GetListener<Person>() {
            @Override
            public void onSuccess(Person person) {
                refreshUserInfo(person);
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("从Bmob上获得用户的刷新数据 失败: " + s);
            }
        });
    }

    private static void setUserInfoProvider () {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                UserInfo userInfo = null;
                UserInfoPreferences pref = UserInfoPreferences.getInstance();
                if (userInfoMap.containsKey(userId)) {
                    userInfo = userInfoMap.get(userId);
                } else if (pref.contains(userId)) {
                    userInfo = pref.getUserInfo(userId);
                    userInfoMap.put(userId, userInfo);
                } else {
                    refreshUserInfoFromCloud(userId);
                }
                return userInfo;
            }
        }, true);
    }

    public static void sendContactNotificationMessage (String operation, String targetId, String message) {
        Person currentUser = Person.getCurrentUser();
        if (currentUser != null) {
            sendContactNotificationMessage(operation, currentUser.getObjectId(), targetId, message);
        }
    }
    public static void sendContactNotificationMessage (String operation, String sourceUserId, String targetUserId, String message) {
        RongIM rong = RongIM.getInstance();
        if (rong != null) {
            ContactNotificationMessage notificationMsg = ContactNotificationMessage.obtain(operation, sourceUserId, targetUserId, message);
            rong.getRongIMClient().sendMessage(
                    Conversation.ConversationType.PRIVATE,
                    targetUserId,
                    notificationMsg,
                    "pushContent",
                    "pushData",
                    new RongIMClient.SendMessageCallback() {

                        @Override
                        public void onSuccess(Integer integer) {
                            logx.e("发送好友通知消息 成功: " + integer);
                        }

                        @Override
                        public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                            logx.e("发送好友通知消息 失败: " + integer + errorCode.getMessage());

                        }
                    },
                    new RongIMClient.ResultCallback<Message>() {
                        @Override
                        public void onSuccess(Message message) {
                            logx.e("发送好友消息通知, 返回结果 成功: " + message.getObjectName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            logx.e("发送好友消息通知, 返回结果 失败: " + errorCode.getMessage());
                        }
                    });
        }
    }

    private static void setSendMessageListener () {
        RongIM rong = RongIM.getInstance();
        if (rong != null) {
            rong.setSendMessageListener(new RongIM.OnSendMessageListener() {
                @Override
                public Message onSend(Message message) {
                    return message;
                }

                @Override
                public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                    if (message.getSentStatus() == Message.SentStatus.SENT) {
                        MessageContent messageContent = message.getContent();
                        if (messageContent instanceof ContactNotificationMessage) {
                            RongIM rong = RongIM.getInstance();
                            if (rong != null) {
                                rong.getRongIMClient().deleteMessages(new int[] {message.getMessageId()});
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    private static void setConversationListBehaviorListener () {
        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {

            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
                // 若点击用户, 则进入用户资料页面
                // 若点击团队, 则进入团队页面
                Intent oppenentActivity = new Intent(context, OpponentActivity.class);
                oppenentActivity.putExtra("userId", s);
                context.startActivity(oppenentActivity);
                return true;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                MessageContent content = uiConversation.getMessageContent();
                RongIM rong = RongIM.getInstance();
                if (content instanceof ContactNotificationMessage) {
                    ContactNotificationMessage message = (ContactNotificationMessage) content;

                    String sourceUserId = message.getSourceUserId();
                    String targetUserId = message.getTargetUserId();
                    Person currentUser = Person.getCurrentUser();
                    if (currentUser != null && sourceUserId.equals(currentUser.getObjectId())) {
                        rong.startPrivateChat(context, targetUserId, null);
                    } else {
                        if (context instanceof ContactActivity) {
                            ContactActivity activity = (ContactActivity) context;

                            if (!activity.hasFriend(targetUserId)) {
                                Intent oppenentIntent = new Intent(context, OpponentActivity.class);
                                oppenentIntent.putExtra("userId", message.getSourceUserId());
                                oppenentIntent.putExtra("action", "response");

                                activity.startActivityForResult(oppenentIntent, 12);
                            } else {
                                rong.startPrivateChat(context, targetUserId, null);
                            }
                        } else {
//                            context.startActivity(oppenentIntent);
                            rong.startPrivateChat(context, targetUserId, null);
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                return false;
            }
        });
    }
}
