package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyTeamActivity extends Activity {
    private Button btn_team_save;
    private TextView tv_team_logo;
    private EditText et_team_name;
    private EditText et_team_slogan;
    private Button btn_team_addmemember;
    private Team myTeamInfo;
    private Context context = this;
    private BmobUser myTeam_bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        init();
        saveTeamInfo();
    }
    //初始化控件
    private void init(){
        btn_team_save = (Button) findViewById(R.id.btn_team_save);
        tv_team_logo = (TextView) findViewById(R.id.tv_team_logo);
        et_team_name = (EditText) findViewById(R.id.et_team_name);
        et_team_slogan = (EditText) findViewById(R.id.et_team_slogan);
        btn_team_addmemember = (Button) findViewById(R.id.btn_team_addmemember);
        myTeamInfo = BmobUser.getCurrentUser(context,Team.class);
    }
    //判断登录状态
    private void saveTeamInfo(){
        myTeam_bmobUser = BmobUser.getCurrentUser(this);
        if(myTeam_bmobUser!=null){

        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void teaminfoClick(View v){
        switch (v.getId()){
            case R.id.btn_team_back:
                finish();
                break;
            case R.id.btn_team_save:
                String myteam_name = et_team_name.getText().toString();
                String myteam_slogan = et_team_slogan.getText().toString();
                if(myteam_name!=null && myteam_slogan!=null && myTeamInfo!=null) {
                    myTeamInfo.setName(myteam_name);
                    myTeamInfo.setMotto(myteam_slogan);
                    myTeamInfo.update(context, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MyTeamActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(MyTeamActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.tv_team_logo:

                break;
            case R.id.btn_team_addmemember:

                break;
        }
    }
}
