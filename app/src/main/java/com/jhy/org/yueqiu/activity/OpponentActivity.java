package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

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
    Button btn_addFriend;

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
                        //iv_info_head.setImageBitmap(bitmap);  //设置对手信息 头像
                        tv_name.setText(person.getUsername());
                        tv_sex.setText(person.getSex() + "");
                        tv_age.setText(person.getAge());
                        tv_height.setText(person.getHeight());
                        tv_weight.setText(person.getWeight());
                        tv_skilled.setText(person.getPosition());
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
        btn_addFriend = (Button) findViewById(R.id.btn_addFriend);
        btn_addFriend.setOnClickListener(click);
    }
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


}
