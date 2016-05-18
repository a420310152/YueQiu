package com.jhy.org.yueqiu.domain;

import android.os.*;
import android.os.Message;

import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.okhttp.internal.Util;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyChallege extends BmobObject {
    private Person person;
    private Challenge challenge;//报名的挑战

    private static Logx logx = new Logx(MyChallege.class);


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public static void query (Challenge challenge, FindListener<MyChallege> listener) {
        if (challenge != null && !Utils.isEmpty(challenge.getObjectId())) {
            BmobQuery<MyChallege> query = new BmobQuery<>();
            query.addWhereEqualTo("challenge", challenge);
            query.include("person,challenge,challenge.initiator");
            query.findObjects(App.getInstance(), listener);
        }
    }

    public static void query (Person person, FindListener<MyChallege> listener) {
        if (person != null && !Utils.isEmpty(person.getObjectId())) {
            BmobQuery<MyChallege> query = new BmobQuery<>();
            query.addWhereEqualTo("person", person);
            query.include("person,challenge,challenge.initiator");
            query.findObjects(App.getInstance(), listener);
        }
    }

    public static void query (Challenge challenge, Person person, FindListener<MyChallege> listener) {
        if (challenge != null && person != null && !Utils.isEmpty(challenge.getObjectId()) && !Utils.isEmpty(person.getObjectId())) {
            BmobQuery<MyChallege> query = new BmobQuery<>();
            query.addWhereEqualTo("person", person);
            query.addWhereEqualTo("challenge", challenge);
            query.include("person,challenge,challenge.initiator");
            query.findObjects(App.getInstance(), listener);
        }
    }

    /*
    * 根据challenge删除所有相关MyChallenge
    * */
    public static void delete (Challenge challenge) {
        query(challenge, new FindListener<MyChallege>() {
            @Override
            public void onSuccess(List<MyChallege> list) {
                for (MyChallege i: list){
                    i.delete(App.getInstance());
                }
            }

            @Override
            public void onError(int i, String s) {
                logx.e("删除失败");
            }
        });
    }

    /*
    * 根据challenge person查询唯一的MyChallenge并删除
    * */
    public static void delete (Challenge challenge, Person person) {
        query(challenge, person, new FindListener<MyChallege>() {
            @Override
            public void onSuccess(List<MyChallege> list) {
                logx.e("删除成功, list.size() == " + list.size());
                for (MyChallege i : list) {
                    i.delete(App.getInstance());
                }
            }

            @Override
            public void onError(int i, String s) {
                logx.e("删除失败 i=" + i + ", s=" + s);
            }
        });
    }

    public static void insert (Challenge challenge, Person person, SaveListener listener) {
        if (challenge != null && person != null && !Utils.isEmpty(challenge.getObjectId()) && !Utils.isEmpty(person.getObjectId())) {
            MyChallege myChallege = new MyChallege();
            myChallege.setPerson(person);
            myChallege.setChallenge(challenge);
            myChallege.save(App.getInstance(), listener);
        }
    }
}
