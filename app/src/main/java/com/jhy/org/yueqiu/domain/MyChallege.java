package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyChallege extends BmobObject{
private String personID;//报名用户
private Challenge challenge;//报名的挑战

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
