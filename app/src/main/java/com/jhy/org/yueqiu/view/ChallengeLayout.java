package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.ResponseChallengeActivity;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.domain.Post;

import java.util.logging.LogRecord;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ChallengeLayout extends LinearLayout{
    TextView tv_type;
    TextView tv_setName;
    TextView tv_setTime;
    TextView tv_setPlace;
    TextView tv_title;
    Context context;
    TextView tv_apply;
    String namePerson; //发起人的姓名
    String namePlace; //发起人选择的地点
    Challenge challenge;
    public ChallengeLayout(Context context) {
        super(context);
        build(context);
    }

    public ChallengeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(context);
    }

    public ChallengeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build(context);
    }
    private void build(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_challenge,this);
        tv_type = (TextView) findViewById(R.id.tv_title);
        tv_setName = (TextView) findViewById(R.id.tv_setName);
        tv_setTime = (TextView) findViewById(R.id.tv_setTime);
        tv_setPlace = (TextView) findViewById(R.id.tv_setPlace);
        tv_title = (TextView) findViewById(R.id.tv_text);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_apply.setOnClickListener(click);
    }
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getContext(),ResponseChallengeActivity.class);
            intent.putExtra("challenge",challenge);
            context.startActivity(intent);
        }
    };
    //利用handler作为中转 在主线程中对控件进行设置
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    tv_setName.setText(namePerson);
                    break;
                case 11:
                    tv_setPlace.setText(namePlace);
                break;
                default:
                    break;
            }
        }
    };
    public void setContent(Challenge challenge){
    //查询Person表的数据 获得发起人的名字
        this.challenge = challenge;
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject(context, challenge.getInitiator().getObjectId(), new GetListener<Person>() {
            @Override
            public void onSuccess(Person person) {
                namePerson = person.getUsername();
                handler.sendEmptyMessage(10);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        //查询Place表的数据 获得发起人选择的地点名字
        BmobQuery<Place> bmobQuery1 = new BmobQuery<Place>();
        bmobQuery1.getObject(context, challenge.getPlace().getObjectId(), new GetListener<Place>() {
            @Override
            public void onSuccess(Place place) {
                namePlace = place.getName();
                handler.sendEmptyMessage(11);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        //由于在列表challenge中是以String类型存在  所以不用特别查询  直接设置
        tv_type.setText(challenge.getType());
        //截取只显示日期的字符
        String data = challenge.getFromDate().getDate();
        data = data.substring(5,16);
        tv_setTime.setText(data);
        tv_title.setText(challenge.getTitle());
    }


}
