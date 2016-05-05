package com.jhy.org.yueqiu.domain;

import com.jhy.org.yueqiu.config.App;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

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

    public static void query (Person master, FindListener<NewFriends> listener) {
        if (master == null || master.getObjectId() == null) {
            return;
        }
        BmobQuery<NewFriends> query = new BmobQuery<>();
        query.addWhereEqualTo("master", master);
        query.include("underFriends");
        query.findObjects(App.getInstance(), listener);
    }

    public static void query (Person me, Person you, FindListener<NewFriends> listener) {
        if (me == null || you == null || me.getObjectId() == null || you.getObjectId() == null) {
            return;
        }
        BmobQuery<NewFriends> query_1 = new BmobQuery<>();
        query_1.addWhereEqualTo("master", me);
        query_1.addWhereEqualTo("underFriends", you);

        BmobQuery<NewFriends> query_2 = new BmobQuery<>();
        query_2.addWhereEqualTo("master", you);
        query_2.addWhereEqualTo("underFriends", me);

        List<BmobQuery<NewFriends>> queryList = new ArrayList<>();
        queryList.add(query_1);
        queryList.add(query_2);

        BmobQuery<NewFriends> query = new BmobQuery<>();
        query.or(queryList);
        query.findObjects(App.getInstance(), listener);
    }
}
