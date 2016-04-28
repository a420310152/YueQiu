package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.NewFriends;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OpponentActivity extends Activity {
    Challenge challenge;
    Person person;
    TextView tv_info_title;
    ImageView iv_info_head;
    TextView tv_name;
    TextView tv_sex;
    TextView tv_age;
    TextView tv_height;
    TextView tv_weight;
    TextView tv_skilled;
    Person currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        build();
        Intent intent = getIntent();
        challenge = (Challenge) intent.getSerializableExtra("challenge");
        Log.i("cha", "OpponentActivity" + challenge);
        person = challenge.getInitiator();
        Bmob.initialize(this, Key.bmob.application_id);
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.getObject(this, challenge.getInitiator().getObjectId(), new
                GetListener<Person>() {
                    @Override
                    public void onSuccess(Person person) {
                        //设置对手信息
                          //设置对手信息 头像
                        if (challenge.getInitiator().getAvatarUrl() != null) {
                            Picasso.with(OpponentActivity.this)
                                    .load(challenge.getInitiator().getAvatarUrl())
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

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("onFailure", "onFailure=====" + s);
                    }
                });

    }

    private void build() {
        tv_info_title = (TextView) findViewById(R.id.tv_info_title);
        iv_info_head = (ImageView) findViewById(R.id.iv_info_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_skilled = (TextView) findViewById(R.id.tv_skilled);
    }


    public void myProfileBackClick(View v) {
        finish();
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

}
