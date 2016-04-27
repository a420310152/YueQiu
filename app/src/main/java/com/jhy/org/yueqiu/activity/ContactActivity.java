package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.test.h.backups.RongUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

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
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ContactActivity extends FragmentActivity implements AdapterView.OnItemClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Context context = this;

    private List<Person> contactList;
    private FriendAdapter contactAdapter;
    private ListView lv_contacts;

    private RelativeLayout fragment_conversationlist;

    private RadioGroup toggle;

    private Person currentUser = null;
    private RongIM rong = RongIM.getInstance();
    private Logx logx = new Logx(ContactActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (currentUser == null) {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }

        contactList = new ArrayList<>();
        contactAdapter = new FriendAdapter(this, contactList);
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setOnItemClickListener(this);
        fillContactList();

        fragment_conversationlist = (RelativeLayout) findViewById(R.id.fragment_conversationlist);

        toggle = (RadioGroup) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(this);

        RongUtils.connect();

        //addFriends();
        enterFragment();
    }

    private void enterFragment () {
        ConversationListFragment fragment = new ConversationListFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
            .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
            .build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_conversationlist, fragment);
        transaction.commit();
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
            //logx.e("按钮点击时间监听成功");
            Person person = contactList.get(position);
            String targetId = person.getObjectId();
            String targetName = person.getUsername();
            logx.e("开启单聊 成功: targetId = " + targetId);
            rong.startPrivateChat(context, targetId, targetName);
        }
    }

    @Override
    public void onClick(View v) {
    }

    //仅供测试用, 添加朋友
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rad_contact) {
            lv_contacts.setVisibility(View.VISIBLE);
            fragment_conversationlist.setVisibility(View.INVISIBLE);
        } else if (checkedId == R.id.rad_conversationList) {
            lv_contacts.setVisibility(View.INVISIBLE);
            fragment_conversationlist.setVisibility(View.VISIBLE);
        }
    }
}
