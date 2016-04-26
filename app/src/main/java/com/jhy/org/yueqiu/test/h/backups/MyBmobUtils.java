package com.jhy.org.yueqiu.test.h.backups;

import com.jhy.org.yueqiu.domain.Person;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20 0020.
 */
public final class MyBmobUtils {
    private static Person currentUser = null;
    private static List<OnReceiveUserInfoListener> userInfoListeners;

    public static Person getCurrentUser () {
        return MyBmobUtils.currentUser;
    }
    public static void setCurrentUser (Person currentUser) {
        MyBmobUtils.currentUser = currentUser;
    }

    public static void registerReceiveUserInfo (OnReceiveUserInfoListener listener) {

    }
}
