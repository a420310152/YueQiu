package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.bmob.BmobUtils;
import com.jhy.org.yueqiu.domain.NewFriends;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.test.h.Test7;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.picasso.Picasso;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.NotificationMessage;
import io.rong.message.TextMessage;

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
    private RelativeLayout selectedUserInfo;
    private ImageView img_avatar;
    private TextView tv_name;

    private ImageButton ibtn_back;
    private ImageButton ibtn_yes;

    private boolean needsResult = false;
    private Person selectedUser = null;
    private View selectedView = null;

    private Person currentUser = null;
    private RongIM rong = RongIM.getInstance();
    private Logx logx = new Logx(ContactActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        currentUser = BmobUtils.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }

        contactList = new ArrayList<>();
        contactAdapter = new FriendAdapter(this, contactList);

        initView();
        //addFriends();
        //confirmToAddAFriend();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillContactList();
        resolveIntent(getIntent());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(ContextMenu.NONE, 1, 1, "删除好友");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        long id = item.getItemId();
        if (id == 1) {
            Person person = contactList.get(position);
            deleteFriend(person);
            contactAdapter.remove(position);
            logx.e("选中Person: " + person.getUsername());
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String targetId = data.getStringExtra("targetId");
            RongIM rong = RongIM.getInstance();
            if (!Utils.isEmpty(targetId) && rong != null) {
                rong.getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, targetId);
            }
        }
    }

    public boolean hasFriend (String userId) {
        for (Person i : contactList) {
            if (i.getObjectId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    private void deleteFriend (Person person) {
        NewFriends.query(currentUser, person, new FindListener<NewFriends>() {
            @Override
            public void onSuccess(List<NewFriends> list) {
                logx.e("查询成功，准备删除联系人 list.size() == " + list.size());
                for (NewFriends i : list) {
                    i.delete(context, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            logx.e("删除联系人 成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            logx.e("删除联系人 失败> i: " + i + ", s: " + s);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                logx.e("查询联系人失败：i:" + i + ", s:" + s);
            }
        });
    }

    private void confirmToAddAFriend () {
        RongUtils.sendContactNotificationMessage("Request", Test7.user.hoge.id, "我是xxx, 请求加你为好友");
    }

    private void initView () {
        toggle = (RadioGroup) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(this);

        selectedUserInfo = (RelativeLayout) findViewById(R.id.selectedUserInfo);
        img_avatar= (ImageView) findViewById(R.id.img_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);

        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);

        ibtn_yes = (ImageButton) findViewById(R.id.ibtn_yes);
        ibtn_yes.setOnClickListener(this);

        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setOnItemClickListener(this);
        registerForContextMenu(lv_contacts);

        fragment_conversationlist = (RelativeLayout) findViewById(R.id.fragment_conversationlist);
    }

    private void resolveIntent (Intent intent) {
        needsResult = intent.getBooleanExtra("message", false);
        toggleView(needsResult);
    }

    private void toggleView (boolean needsResult) {
        if (needsResult) {
            toggle.setVisibility(View.GONE);
            selectedUserInfo.setVisibility(View.VISIBLE);
            ibtn_yes.setVisibility(selectedUser == null ? View.GONE : View.VISIBLE);
        } else {
            toggle.setVisibility(View.VISIBLE);
            selectedUserInfo.setVisibility(View.GONE);
            ibtn_yes.setVisibility(View.GONE);
            enterFragment();
        }
    }

    private void enterFragment () {
        ConversationListFragment fragment = new ConversationListFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_conversationlist, fragment);
        transaction.commit();
    }

    private void fillContactList () {
        NewFriends.query(currentUser, new FindListener<NewFriends>() {
            @Override
            public void onSuccess(List<NewFriends> list) {
                if (!Utils.isEmpty(list)) {

                    contactList.clear();
                    for (NewFriends i : list) {
                        contactList.add(i.getUnderFriends());
                    }
                    lv_contacts.setAdapter(contactAdapter);
                }
            }

            @Override
            public void onError(int i, String s) {
                logx.e("填充联系人 错误>> 错误码: " + i + ", 错误描述: " + s);
            }
        });
    }

    private void returnResult () {
        if (needsResult) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", selectedUser);
            setResult(selectedUser == null ? RESULT_CANCELED : RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.ibtn_back || id == R.id.ibtn_yes) {
            returnResult();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person user = contactList.get(position);
        String userId = user.getObjectId();
        String userName = user.getUsername();
        String userAvatar = user.getAvatarUrl();

        if (needsResult) {
            selectedUser = user;
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.TRANSPARENT);
            }
            view.setBackgroundColor(0x99AABBCC);
            selectedView = view;
            ibtn_yes.setVisibility(View.VISIBLE);

            if (!Utils.isEmpty(userAvatar)) {
                Picasso.with(context)
                        .load(userAvatar)
                        .transform(new RoundTransform())
                        .into(img_avatar);
            }
            tv_name.setText(userName);
        } else if (rong != null) {
            logx.e("开启单聊 成功: targetId = " + userId);
            rong.startPrivateChat(context, userId, userName);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rad_contact) {
            lv_contacts.setVisibility(View.VISIBLE);
            fragment_conversationlist.setVisibility(View.INVISIBLE);
            toggle.setBackgroundResource(R.drawable.icon_toggle_left);

        } else if (checkedId == R.id.rad_conversationList) {
            lv_contacts.setVisibility(View.INVISIBLE);
            fragment_conversationlist.setVisibility(View.VISIBLE);
            toggle.setBackgroundResource(R.drawable.icon_toggle_right);
        }
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
}
