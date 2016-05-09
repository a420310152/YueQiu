package com.jhy.org.yueqiu.utils;

import android.net.Uri;

import com.jhy.org.yueqiu.config.App;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/5/7 0007.
 */

public class UserInfoCache {
    private static final String PREFERENCES_NAME = "yueqiu.user_info";
    private static final String USER_SET = "user_set";
    private static final String USER_NAME = "user_name_";
    private static final String USER_PORTRAIT_URI = "user_portrait_uri_";

    private static UserInfoCache singleton = null;

    private Preferences preferences;
    private Set<String> changedSet;
    private Set<String> userSet;
    private HashMap<String, UserInfo> userMap;

    private UserInfoCache() {
        preferences = new Preferences(PREFERENCES_NAME);

        userSet = preferences.get(USER_SET, new HashSet<String>());
        userMap = new HashMap<>();
        changedSet = new HashSet<>();
    }

    public static UserInfoCache getInstance () {
        if (singleton == null) {
            singleton = new UserInfoCache();
        }
        return singleton;
    }

    public UserInfo getUserInfo (String userId) {
        UserInfo userInfo = null;
        if (userMap.containsKey(userId)) {
            userInfo = userMap.get(userId);
        } else if (userSet.contains(userId)) {
            String userName = preferences.get(USER_NAME + userId);
            String portraitUri = preferences.get(USER_PORTRAIT_URI + userId);

            userInfo = new UserInfo(userId, userName, Uri.parse(portraitUri));
            userMap.put(userId, userInfo);
        }
        return userInfo;
    }

    public UserInfoCache setUserInfo (UserInfo userInfo) {
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            changedSet.add(userId);
            userSet.add(userId);
            userMap.put(userId, userInfo);
        }
        return this;
    }

    public UserInfoCache apply () {
        if (changedSet.size() > 0) {
            preferences.set(USER_SET, userSet);
            for (String i : changedSet) {
                if (userMap.containsKey(i)) {
                    UserInfo userInfo = userMap.get(i);
                    String name = userInfo.getName();
                    String portraitUri = userInfo.getPortraitUri().toString();

                    preferences.set(USER_NAME + i, name);
                    preferences.set(USER_PORTRAIT_URI + i, portraitUri);
                } else {
                    preferences.remove(USER_NAME + i);
                    preferences.remove(USER_PORTRAIT_URI + i);
                }
            }
            preferences.commit();
            changedSet.clear();
        }
        return this;
    }

    public boolean contains (String userId) {
        return userSet.contains(userId);
    }

    public UserInfoCache remove (String userId) {
        if (contains(userId)) {
            changedSet.add(userId);
            userSet.remove(userId);
            userMap.remove(userId);
        }
        return this;
    }

    public void clear () {
        preferences.clear();
    }
}