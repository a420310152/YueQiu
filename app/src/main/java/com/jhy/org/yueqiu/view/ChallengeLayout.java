package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.OpponentActivity;
import com.jhy.org.yueqiu.activity.ResponseChallengeActivity;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.LogRecord;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ChallengeLayout extends LinearLayout {
    ImageView iv_head;
    TextView tv_type;
    TextView tv_setName;
    TextView tv_setTime;
    TextView tv_setPlace;
    TextView tv_title;
    ImageView tv_apply;
    TextView tv_apply_text;
    Context context;
    Challenge challenge;
    Person person;

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

    private void build(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_challenge, this);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_type = (TextView) findViewById(R.id.tv_title);
        tv_setName = (TextView) findViewById(R.id.tv_setName);
        tv_setTime = (TextView) findViewById(R.id.tv_setTime);
        tv_setPlace = (TextView) findViewById(R.id.tv_setPlace);
        tv_title = (TextView) findViewById(R.id.tv_text);
        tv_apply = (ImageView) findViewById(R.id.tv_apply);
        tv_apply_text = (TextView) findViewById(R.id.tv_apply_text);
        iv_head.setOnClickListener(headClick);
        tv_apply_text.setOnClickListener(textClick);
    }

    //点击头像弹出个人资料
    View.OnClickListener headClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String type = challenge.getType();
            Intent intent = new Intent(context, OpponentActivity.class);
            intent.putExtra("person", challenge.getInitiator());
            Log.i("rea", "challenge===========" + challenge.getInitiator());
            context.startActivity(intent);
        }
    };

    public void setContent(Challenge challenge, Boolean b) {//传此两个参数，第二个boolean值为true为默认 false为设置成查看报名
        this.challenge = challenge;
        //开始设置每一个挑战的发起人头像  此时需要每次都向服务器获取
        if (challenge == null || b == null) {
            return;
        }
//        Log.i("getAvatarUrl", "challenge.getInitiator().getAvatarUrl()===" + challenge.getInitiator().getAvatarUrl());
        if (challenge.getInitiator() != null) {
            Picasso.with(context)
                    .load(challenge.getInitiator().getAvatarUrl())
                    .transform(new RoundTransform())
                    .into(iv_head);
        }
        //获得发起人的名字
        if(challenge.getInitiator()!=null){
            tv_setName.setText(challenge.getInitiator().getUsername());

        }
        // 获得发起人选择的地点名字

        String placeString = challenge.getPlaceName() + "";
        Log.i("placeString", "placeString=====" + placeString);
        if (!Utils.isEmpty(placeString) && placeString.length() > 9) {
            tv_setPlace.setText(placeString.substring(0, 9) + "...");
            Log.i("placeString", "Tag11111=====" + placeString);
        } else if (!Utils.isEmpty(placeString)) {
            tv_setPlace.setText(placeString);
            Log.i("placeString", "Tag22222=====" + placeString);
        }
        //由于在列表challenge中是以String类型存在  所以不用特别查询  直接设置
        tv_type.setText(challenge.getType());
        //截取只显示日期的字符
        Log.i("data", "challenge===============" + challenge);
        Log.i("data", "challenge.getFromDate()===============" + challenge.getFromDate());
        String data = challenge.getFromDate().getDate();
        data = data.substring(5, 16);
        tv_setTime.setText(data);
        tv_title.setText(challenge.getTitle());
        //设置是否显示查看报名按钮
        if (b == true) {
            tv_apply.setOnClickListener(click);
        } else {
            tv_apply.setVisibility(INVISIBLE);
            tv_apply_text.setVisibility(VISIBLE);
        }
    }

    //点击约战报名按钮
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //跳转至报名页面
            person = BmobUser.getCurrentUser(context, Person.class);//得到当前用户的对象
            if (person != null) {
                Intent intent = new Intent(context, ResponseChallengeActivity.class);
                intent.putExtra("challenge", challenge);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "请登录账号", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //点击查看报名按钮
    OnClickListener textClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //跳转至报名页面
            person = BmobUser.getCurrentUser(context, Person.class);//得到当前用户的对象
            if (person != null) {
                Intent intent = new Intent(context, ResponseChallengeActivity.class);
                intent.putExtra("challenge", challenge);
                intent.putExtra("checkApply", 1);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "请登录账号", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
