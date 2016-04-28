package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/4/28.
 */
public class NewFriends extends BmobObject {
    private Person master;  //归属人
    private Person underFriends;    //未添加的朋友


    public Person getUnderFriends() {
        return underFriends;
    }

    public void setUnderFriends(Person underFriends) {
        this.underFriends = underFriends;
    }

    public Person getMaster() {
        return master;
    }

    public void setMaster(Person master) {
        this.master = master;
    }
}
