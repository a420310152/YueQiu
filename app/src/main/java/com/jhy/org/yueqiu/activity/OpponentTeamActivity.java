package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.TeamAdapter;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.view.TeamLayout;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 *          作者：夏旺
 * Created by Administrator on 2016/4/18.
 */
public class OpponentTeamActivity extends Activity{
    Challenge challenge;
    ImageView iv_team_logo;//球队头像
    TextView tv_teamname_text;//队名
    TextView tv_motto_text;//球队宣言
    TeamLayout comstomlayout_creator;//队长
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_team);
        Intent intent = getIntent();
        challenge = (Challenge) intent.getSerializableExtra("challenge");
        build();
    }
    private void build(){
        iv_team_logo = (ImageView) findViewById(R.id.iv_team_logo);
        tv_teamname_text = (TextView) findViewById(R.id.tv_teamname_text);
        tv_motto_text = (TextView) findViewById(R.id.tv_motto_text);
        comstomlayout_creator = (TeamLayout) findViewById(R.id.comstomlayout_creator);
        listview = (ListView) findViewById(R.id.listview);

        //查询Bmob数据 设置页面信息
        Bmob.initialize(this, Key.bmob.application_id);
        Person creator = challenge.getInitiator();//获得队长对象
        comstomlayout_creator.setTeam(creator);//设置队长信息

        //根据队长 来查询队伍
        final BmobQuery<Team> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("creator",creator);
        bmobQuery.findObjects(this, new FindListener<Team>() {
            @Override
            public void onSuccess(List<Team> list) {
                Team team = list.get(0);//得到Team对象
                tv_motto_text.setText(team.getMotto());//设置球队宣言
                tv_teamname_text.setText(team.getName());
                /*
                * 此处可以设置球队页面 球队信息
                * */
                BmobQuery<Person> bmobQuery1 = new BmobQuery<Person>();
                bmobQuery1.addWhereRelatedTo("members",new BmobPointer(team));
                bmobQuery1.findObjects(OpponentTeamActivity.this, new FindListener<Person>() {
                    @Override
                    public void onSuccess(List<Person> list) {
                        Log.i("list","list====="+list.size());
                        TeamAdapter adapter = new TeamAdapter(OpponentTeamActivity.this,list);
                        listview.setAdapter(adapter);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
