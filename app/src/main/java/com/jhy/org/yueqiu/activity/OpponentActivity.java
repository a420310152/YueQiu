package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.NewFriends;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;
import io.rong.message.ContactNotificationMessage;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OpponentActivity extends Activity implements View.OnClickListener {
    private Context context = this;

    Person person;
    TextView tv_info_title;
    ImageView iv_info_head;
    TextView tv_name;
    TextView tv_sex;
    TextView tv_age;
    TextView tv_height;
    TextView tv_weight;
    TextView tv_skilled;
    Button btn_addFriend;
    Button btn_sendMessage;
    Person currentUser;

    private Person targetPerson;
    private String actionOfIntent;

    private static Logx logx = new Logx(OpponentActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        build();
        getIntentData(getIntent());
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.btn_addFriend) {
            onClickAddFriend();
        } else if (id == R.id.btn_sendMessage) {
            onClickSendMessage();
        }
    }

    private void build() {
        iv_info_head = (ImageView) findViewById(R.id.iv_info_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_skilled = (TextView) findViewById(R.id.tv_skilled);

        btn_addFriend = (Button) findViewById(R.id.btn_addFriend);
        btn_addFriend.setVisibility(View.GONE);
        btn_addFriend.setOnClickListener(this);

        btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);
        btn_sendMessage.setOnClickListener(this);
    }

    private void getIntentData (Intent intent) {
        String userId = intent.getStringExtra("userId");
        Person person = (Person) intent.getSerializableExtra("person");
        String action  = intent.getStringExtra("action");

        this.actionOfIntent = action;
        if (person != null) {
            checkIfSelf(person.getObjectId());
            setOpponent(person);
        } else if (!Utils.isEmpty(userId)) {
            checkIfSelf(userId);
            queryPerson(userId);
        } else {
            finish();
        }
    }

    private void queryPerson (String userId) {
        BmobQuery<Person> query = new BmobQuery<>();
        query.getObject(context, userId, new GetListener<Person>() {
            @Override
            public void onSuccess(Person person) {
                setOpponent(person);
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("查询用户信息 失败: " + s);
            }
        });
    }

    private void checkIfSelf (String userId) {
        String currentUserId = (String) BmobUser.getObjectByKey(context, "objectId");
        if (Utils.equals(currentUserId, userId)) {
            Intent myProfileIntent = new Intent(context, MyProfileActivity.class);
            startActivity(myProfileIntent);
            finish();
        }
    }

    private void setOpponent(Person person){
        if (person == null) {
            return;
        }
        this.targetPerson = person;
        checkContact(person);
        RongUtils.refreshUserInfo(person);

        //设置对手信息
        if (person.getAvatarUrl() != null) {
            Picasso.with(OpponentActivity.this)
                    .load(person.getAvatarUrl())
                    .transform(new RoundTransform())
                    .into(iv_info_head);
        }
        if (person.getUsername()!=null){
            tv_name.setText(person.getUsername());
        }
        if (person.getSex()!=null){
            tv_sex.setText(person.getSex() ? "男" : "女");
        }
        if (person.getAge()!=null){
            tv_age.setText(person.getAge() + " 岁");
        }
        if (person.getHeight()!=null){
            tv_height.setText(person.getHeight() + " cm");
        }
        if (person.getWeight()!=null){
            tv_weight.setText(person.getWeight() + " kg");
        }
        if (person.getPosition()!=null){
            tv_skilled.setText(person.getPosition());
        }
    }

    private void checkIfLogined () {
        Person currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (currentUser == null) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivity(loginIntent);
            showToast("请先登录");
        }
    }

    private void onClickAddFriend () {
        Person person = targetPerson;
        checkIfLogined();

        if (Utils.equals(actionOfIntent, "request")) {
            requestToAddFriend(person);
            showToast("已发送好友请求!");
            finish();
        } else if (Utils.equals(actionOfIntent, "response")) {
            responseToAddFriend(person);
        } else {
            requestToAddFriend(person);
            showToast("已发送好友请求!");
            finish();
        }
    }

    private void onClickSendMessage () {
        Person person = targetPerson;
        checkIfLogined();
        RongIM rong = RongIM.getInstance();
        if (rong != null) {
            rong.startPrivateChat(context, person.getObjectId(), null);
        }
    }

    // 检查是否是好友关系
    private void checkContact(Person person) {
        Person currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (person == null || currentUser == null) {
            logx.e("checkContact 异常: 未登录, 或好友为空");
            return;
        }
        BmobQuery<NewFriends> query = new BmobQuery<>();
        query.addWhereEqualTo("master", person);
        query.addWhereEqualTo("underFriends", currentUser);
        query.findObjects(context, new FindListener<NewFriends>() {
            @Override
            public void onSuccess(List<NewFriends> list) {
                if (Utils.isEmpty(list)) {
                    btn_addFriend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int i, String s) {
                logx.e("查询是否为已添加好友 失败: " + s);
            }
        });
    }

    private void returnResult () {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("targetId", targetPerson.getObjectId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //响应添加一个好友
    private void responseToAddFriend (Person person) {
        Person currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (currentUser == null || person == null) {
            logx.e("addFriend 异常: 未登录, 或添加的好友为空");
            return;
        }

        NewFriends insert_0 = new NewFriends();
        insert_0.setMaster(currentUser);
        insert_0.setUnderFriends(person);

        NewFriends insert_1 = new NewFriends();
        insert_1.setMaster(person);
        insert_1.setUnderFriends(currentUser);

        List<BmobObject> list = new ArrayList<>();
        list.add(insert_0);
        list.add(insert_1);

        new BmobObject().insertBatch(context, list, new SaveListener() {
            @Override
            public void onSuccess() {
                showToast("已成功添加好友, 开始和ta聊天吧!");
                btn_addFriend.setVisibility(View.GONE);
                returnResult();
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("抱歉, 添加好友失败!");
                logx.e("添加好友 失败: " + s);
            }
        });
    }

    //请求添加好友
    private void requestToAddFriend (Person person) {
        logx.e("requestToAddFriend");
        RongUtils.sendContactNotificationMessage("Request", person.getObjectId(), "您好， " + person.getUsername() + "请求添加您为好友!");
    }

    //点击加为好友 向Bmob中Contact表中添加newfriends
    public void addFriend(View v) {
        Log.i("addFriend", "addFriend==============");
        currentUser = BmobUser.getCurrentUser(this, Person.class);
        if (currentUser != null) {
            //首先在Bmob中获取newfriends中master和underfriends是否已经有
            BmobQuery<NewFriends> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("master", person);//一个条件一个query
            BmobQuery<NewFriends> bmobQuery1 = new BmobQuery<>();
            bmobQuery1.addWhereEqualTo("underFriends", currentUser);//一个条件一个query
            List<BmobQuery<NewFriends>> bmobQueryList = new ArrayList<BmobQuery<NewFriends>>();
            bmobQueryList.add(bmobQuery);
            bmobQueryList.add(bmobQuery1);//讲两个条件加入条件数组
            BmobQuery<NewFriends> bmobQueryMain = new BmobQuery<>();
            bmobQueryMain.and(bmobQueryList);//新建一个查询，将条件数组添加进入
            bmobQueryMain.findObjects(this, new FindListener<NewFriends>() {
                @Override
                public void onSuccess(List<NewFriends> list) {
                    Log.i("NewFriends", "NewFriends===========" + list.size());
                    NewFriends newFriends = null;
                    if (Utils.isEmpty(list)) {
                        Log.i("newFriends", "onFailure=======");
                        newFriends = new NewFriends();
                        newFriends.setMaster(person);
                        newFriends.setUnderFriends(currentUser);
                        newFriends.save(OpponentActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(OpponentActivity.this, "申请好友成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    } else {
                        Toast.makeText(OpponentActivity.this, "您已经申请，不要重复申请哦！", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });


        } else {
            Toast.makeText(this, "请登录账号", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast (CharSequence text) {
        Toast.makeText(OpponentActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
