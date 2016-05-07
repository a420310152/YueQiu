package com.jhy.org.yueqiu.utils;

import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/5/7 0007.
 */

public final class UserInfoPreferences extends Preferences {
    private static final String PREFERENCES_NAME = "yueqiu.user_info";
    private static final String USER_SET = "user_set";
    private static final String USER_NAME = "user_name_";
    private static final String USER_PORTRAIT_URI = "user_portrait_uri_";
    private static Set<String> userSet;
    private static UserInfoPreferences singleton = null;

    protected UserInfoPreferences() {
        super(PREFERENCES_NAME);
        userSet = get(USER_SET, new HashSet<String>());
    }

    public static UserInfoPreferences getInstance () {
        if (singleton == null) {
            singleton = new UserInfoPreferences();
        }
        return singleton;
    }

    public UserInfo getUserInfo (String userId) {
        if (contains(userId)) {
            String userName = get(USER_NAME + userId);
            String portraitUri = get(USER_PORTRAIT_URI + userId);
            return new UserInfo(userId, userName, Uri.parse(portraitUri));
        }
        return null;
    }

    public UserInfoPreferences setUserInfo (UserInfo userInfo) {
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            userSet.add(userId);
            set(USER_SET, userSet);
            set(USER_NAME + userId, userInfo.getName());
            set(USER_PORTRAIT_URI + userId, userInfo.getPortraitUri().toString());
        }
        return this;
    }

    @Override
    public boolean contains (String userId) {
        return userSet.contains(userId);
    }

    @Override
    public Preferences remove (String userId) {
        if (userSet.contains(userId)) {
            userSet.remove(userId);
            set(USER_SET, userSet);
            remove(USER_NAME + userId);
            remove(USER_PORTRAIT_URI + userId);
        }
        return this;
    }
}