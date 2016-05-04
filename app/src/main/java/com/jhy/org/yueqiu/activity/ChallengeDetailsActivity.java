package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.ChallengeAdapter;
import com.jhy.org.yueqiu.domain.Challenge;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ChallengeDetailsActivity extends Activity{
    ListView lv_war;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_challenge);

    }

    @Override
    protected void onStart() {
        super.onStart();
        build();
    }

private List<Challenge> challengeList = new ArrayList<>();
private ChallengeAdapter challengeAdapter = null;
    private void build(){
        lv_war = (ListView) findViewById(R.id.lv_war);
        challengeAdapter = new ChallengeAdapter(challengeList,this,true);
        lv_war.setOnItemClickListener(itemClick);
        BmobQuery<Challenge> query = new BmobQuery<Challenge>();
        query.setLimit(20);
        query.order("-createdAt");//设置按照时间大小降序排列
        query.include("initiator");
        query.findObjects(this, new FindListener<Challenge>() {
            @Override
            public void onSuccess(List<Challenge> list) {
                challengeList.clear();
                challengeList.addAll(list);
                if (lv_war.getAdapter()!=null){
                    challengeAdapter.notifyDataSetChanged();
                }else {
                    lv_war.setAdapter(challengeAdapter);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    //点击Item项事件 弹出对手信息
    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Challenge challenge = (Challenge) parent.getItemAtPosition(position);
            String type = challenge.getType();
            Intent intent;

            if (type.equals(Challenge.TYPE_SOLO) || type.equals(Challenge.TYPE_TRAIN)) {
                intent = new Intent(ChallengeDetailsActivity.this, OpponentActivity.class);
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            } else if (type.equals(Challenge.TYPE_TEAM)) {
                intent = new Intent(ChallengeDetailsActivity.this, OpponentTeamActivity.class);
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            }

        }
    };
    public void loginMenu(View v){
        finish();
    }
    public void referensh(View v){
        BmobQuery<Challenge> query = new BmobQuery<Challenge>();
        query.setLimit(20);
        query.order("-createdAt");//设置按照时间大小降序排列
        query.include("initiator");
        query.findObjects(this, new FindListener<Challenge>() {
            @Override
            public void onSuccess(List<Challenge> list) {
                challengeList.clear();
                challengeList.addAll(list);
                lv_war.setAdapter(challengeAdapter);
                Toast.makeText(ChallengeDetailsActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ChallengeDetailsActivity.this,"刷新失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
