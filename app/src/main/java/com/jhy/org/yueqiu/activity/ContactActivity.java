package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.test.h.backups.RongUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ContactActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context = this;

    private List<Person> contactList;
    private FriendAdapter contactAdapter;
    private ListView lv_contacts;

    private Person currentUser = null;
    private RongIM rong = RongIM.getInstance();
    private Logx logx = new Logx(ContactActivity.class);
    private Intent chatRoomIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (currentUser == null) {
            startActivity(new Intent(context, LoginActivity.class));
        }

        contactList = new ArrayList<Person>();
        contactAdapter = new FriendAdapter(this, contactList);
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setOnItemClickListener(this);
        fillContactList();

        chatRoomIntent = new Intent(context, ChatRoomActivity.class);
        RongUtils.connect();
    }

    private void fillContactList () {
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereRelatedTo("friends", new BmobPointer(currentUser));
        query.findObjects(context, new FindListener<Person>() {
            @Override
            public void onSuccess(List<Person> list) {
                logx.e("成功, fillContactlist");
                contactList.addAll(list);
                lv_contacts.setAdapter(contactAdapter);
            }

            @Override
            public void onError(int i, String s) {
                logx.e("失败, i: " + i + ", s: " + s);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (rong != null) {
            logx.e("按钮点击时间监听成功");
            rong.startPrivateChat(context, contactList.get(position).getObjectId(), null);
            startActivity(chatRoomIntent);
        }
    }

    @Override
    public void onClick(View v) {
 /*       switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_options:
                if (RongIM.getInstance() != null)
                *//**
         *创建讨论组时，mLists为要添加的讨论组成员，创建者一定不能在 mLists 中
         *//*
                    RongIM.getInstance().getRongIMClient().createDiscussion("Hello Discussion", concatList, new RongIMClient.CreateDiscussionCallback() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                break;
            default:
                break;
        }*/
    }

    private void addFriends () {
        List<String> list = Arrays.asList("6b3b2f0bb9", "aeae59b2c4", "96daf1a81f", "d656c23eff", "70ae5d7078", "622cdb9543", "abf9a4ef71");
        BmobRelation relation = new BmobRelation();
        for (String id : list) {
            Person friend = new Person();
            friend.setObjectId(id);
            relation.add(friend);
        }
        currentUser.setFriends(relation);
        currentUser.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                logx.e("成功, 添加朋友");
                fillContactList();
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("失败, 添加朋友");
            }
        });
    }
}
