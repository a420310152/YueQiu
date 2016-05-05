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
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.AllTeamLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MyAllTeamActivity extends Activity {
    private AllTeamLayout allTeamLayout;
    private AllTeamAdapter allTeamAdapter;
    private ListView lv_allteam_info;
    private Team team;//我创建的球队
    private BmobUser my_allTeam_bmobuser;
    private Button btn_add_team;//创建球队按钮
    private Person person;//当前用户
    private List<Person> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allteam);
        judgeLogin();
        queryTeam();
        addTeam();
    }

    //判断登录状态
    private void judgeLogin() {
        my_allTeam_bmobuser = BmobUser.getCurrentUser(this);
        if (my_allTeam_bmobuser != null) {
            init();
        } else {
            startActivity(new Intent(MyAllTeamActivity.this, LoginActivity.class));
            finish();
        }
    }

    //初始化控件
    private void init() {
        allTeamLayout = (AllTeamLayout) findViewById(R.id.allteam_mode);
        lv_allteam_info = (ListView) findViewById(R.id.lv_allteam_info);
        btn_add_team = (Button) findViewById(R.id.btn_add_team);
        person = BmobUser.getCurrentUser(MyAllTeamActivity.this, Person.class);
    }

    //判断我是否创建过球队并查询
    private void queryTeam() {
        BmobQuery<Team> query = new BmobQuery<>();
        query.addWhereEqualTo("creator", new BmobPointer(person));
        query.findObjects(this, new FindListener<Team>() {
            @Override
            public void onSuccess(final List<Team> list) {
                if (!Utils.isEmpty(list)) {
                    team = list.get(0);
                    btn_add_team.setVisibility(View.INVISIBLE);
                    allTeamLayout.setBuildTeam(team);
                    allTeamLayout.setVisibility(View.VISIBLE);
                    allTeamLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent allTeamIntent = new Intent(MyAllTeamActivity.this, MyTeamActivity.class);
                            allTeamIntent.putExtra("team", team);
                            startActivity(allTeamIntent);
                        }
                    });
                } else {
                    btn_add_team.setVisibility(View.VISIBLE);
                    allTeamLayout.setVisibility(View.INVISIBLE);
                    btn_add_team.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myTeamIntent = new Intent(MyAllTeamActivity.this, MyTeamActivity.class);
                            startActivity(myTeamIntent);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //查询我加入的球队
    private void addTeam() {
        BmobQuery<Team> query = new BmobQuery<Team>();
        query.addWhereEqualTo("addTeam", new BmobPointer(team));
        query.findObjects(this, new FindListener<Team>() {

            @Override
            public void onSuccess(List<Team> list) {
                for (final Team addteam : list) {
                    allTeamLayout.setAllTeamInfo(addteam);
                    allTeamAdapter = new AllTeamAdapter(MyAllTeamActivity.this,list);
                    lv_allteam_info.setAdapter(allTeamAdapter);
                    lv_allteam_info.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent addTeamIntent = new Intent(MyAllTeamActivity.this, MyTeamActivity.class);
                            addTeamIntent.putExtra("addteam", addteam);
                            startActivity(addTeamIntent);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
