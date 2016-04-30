package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.ApplyAdapter;
import com.jhy.org.yueqiu.adapter.ChallengeAdapter;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class MyApplyActivity extends Activity{
    private ChallengeAdapter challengeAdapter;
    private ListView lv_apply_info;
    private BmobUser my_apply_bmobuser;
    private Context context = this;
    private Challenge challenge;
    private Person person;//当前用户
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
        lv_apply_info = (ListView) findViewById(R.id.lv_apply_info);
        person = BmobUser.getCurrentUser(MyApplyActivity.this, Person.class);
        judgeLogin();
        applyChallenge();
    }
    //查询我报名的挑战
    private void applyChallenge(){
        BmobQuery<Challenge> query = new BmobQuery<>();
        //query.addWhereContainedIn("responders",person.getO);
        query.findObjects(this, new FindListener<Challenge>() {

            @Override
            public void onSuccess(List<Challenge> list) {
                challengeAdapter = new ChallengeAdapter( list,MyApplyActivity.this);
                challengeAdapter.setTv_apply_GONE();
                lv_apply_info.setAdapter(challengeAdapter);
                Log.i("1onSuccess", "填充需要我的报名的list" + list);
            }

            @Override
            public void onError(int code, String msg) {
                Log.i("1onSuccess", "查询失败list" + msg);
            }
        });
    }

    //判断登录状态并对报名列表进行监听
    private void judgeLogin(){
        my_apply_bmobuser = BmobUser.getCurrentUser(context);
        if(my_apply_bmobuser!=null){
            lv_apply_info.setOnItemClickListener(click);
        }else{
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }
    OnItemClickListener click = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Challenge challenge = (Challenge) parent.getItemAtPosition(position);
            String type = challenge.getType();
            Intent intent;

            if (type.equals(Challenge.TYPE_SOLO) || type.equals(Challenge.TYPE_TRAIN)) {
                intent = new Intent(MyApplyActivity.this, OpponentSoloActivity.class);
                intent.putExtra("challenge", challenge);
                Log.i("rea", "challenge===========" + challenge.getInitiator());
                startActivity(intent);
            } else if (type.equals(Challenge.TYPE_TEAM)) {
                intent = new Intent(MyApplyActivity.this, OpponentTeamActivity.class);
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            }
        }
    };

    //返回按钮的监听
    public void myApplyBackClick(View v){
        finish();
    }
}
