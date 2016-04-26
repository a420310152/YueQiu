package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.AllTeamAdapter;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.view.AllTeamLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MyAllTeamActivity extends Activity{
    private AllTeamLayout allTeamLayout;
    private AllTeamAdapter allTeamAdapter;
    private ListView lv_allteam_info;
    private Team team;
    private List<Team> list;
    private BmobUser my_allTeam_bmobuser;
    private Button btn_add_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allteam);
        judgeLogin();
        buildTeam(team);
    }
    //判断登录状态
    private void judgeLogin(){
        my_allTeam_bmobuser = BmobUser.getCurrentUser(this);
        if(my_allTeam_bmobuser!=null){
            init();
        }else{
            startActivity(new Intent(MyAllTeamActivity.this, LoginActivity.class));
            finish();
        }
    }

    //判断我是否创建过球队
    private void buildTeam(Team team){
        team = new Team();
        Person person = BmobUser.getCurrentUser(MyAllTeamActivity.this, Person.class);
        if(team.getCreator()!=person){
            btn_add_team.setVisibility(View.VISIBLE);
            allTeamLayout.setVisibility(View.INVISIBLE);
            btn_add_team.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAddTeam = new Intent(MyAllTeamActivity.this, MyTeamActivity.class);
                    startActivity(intentAddTeam);
                }
            });
        }else{
            if(btn_add_team!=null && allTeamLayout!=null){
                btn_add_team.setVisibility(View.INVISIBLE);
                allTeamLayout.setVisibility(View.VISIBLE);
            }
        }
    }
    //返回按钮监听
    public void allteaminfoClick(View v){
        finish();
    }
    //初始化控件
   private void init(){
        allTeamLayout = (AllTeamLayout) findViewById(R.id.allteam_mode);
        lv_allteam_info = (ListView) findViewById(R.id.lv_allteam_info);
        btn_add_team = (Button) findViewById(R.id.btn_add_team);

       /* list = new ArrayList<Team>();
        list.add(team);*/


       allTeamLayout.setOnClickListener(onClick);
        lv_allteam_info.setOnItemClickListener(click);
    }

    /*private void addTeam(){
        BmobQuery<Team> query = new BmobQuery<Team>();
        Team team = new Team();
        query.addWhereEqualTo("team",new BmobPointer(team));
        query.include("name,logo,creator,members,motto");
        query.findObjects(this, new FindListener<Team>() {
            @Override
            public void onSuccess(List<Team> list) {
                allTeamAdapter = new AllTeamAdapter(MyAllTeamActivity.this,list);
                lv_allteam_info.setAdapter(allTeamAdapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }*/
    //对我创建的球队进行监听
    OnClickListener onClick = new OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent allTeamIntent = new Intent(MyAllTeamActivity.this,MyTeamActivity.class);
            startActivity(allTeamIntent);
        }
    };
    //对我加入的球队列表进行监听
    OnItemClickListener click = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
}
