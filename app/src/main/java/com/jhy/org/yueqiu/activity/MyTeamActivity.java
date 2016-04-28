package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyTeamActivity extends Activity {
    private Button btn_team_save;
    private ImageView iv_team_logo;
    private EditText et_team_name;
    private EditText et_team_slogan;
    private Button btn_team_addmemember;
    //private Team myTeamInfo;
    private Team team;
    private Context context = this;
    private BmobUser myTeam_bmobUser;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        init();
        saveTeamInfo();
        shoeInfo();
    }
    //初始化控件
    private void init(){
        btn_team_save = (Button) findViewById(R.id.btn_team_save);
        iv_team_logo = (ImageView) findViewById(R.id.iv_team_logo);
        et_team_name = (EditText) findViewById(R.id.et_team_name);
        et_team_slogan = (EditText) findViewById(R.id.et_team_slogan);
        btn_team_addmemember = (Button) findViewById(R.id.btn_team_addmemember);
        imageLoader = new ImageLoader(MyTeamActivity.this,iv_team_logo);
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
    //进入我的球队界面显示输入框中的信息
        private void shoeInfo(){
            Intent intent =getIntent();
            team = (Team) intent.getSerializableExtra("team");
            Log.e("result","~~~~~~~~~~~~~~~~~~~~~~~123"+team);
               et_team_name.setText(team.getName());
               et_team_slogan.setText(team.getMotto());
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageLoader != null) {
            imageLoader.setResult(requestCode, data);
        }
    }
    //保存和返回按钮的监听
    public void teaminfoClick(View v){
        switch (v.getId()){
            case R.id.btn_team_back:
                finish();
                break;
            case R.id.btn_team_save:
                team = new Team();
                    String myteam_name = et_team_name.getText().toString();
                    String myteam_slogan = et_team_slogan.getText().toString();
                    String path = Environment.getExternalStorageDirectory() + "logo.jpg";
                    BmobFile file = new BmobFile(new File(path));
                if(myteam_name!=null && myteam_slogan!=null) {
                    //team.setLogo(file);
                    team.setCreator(BmobUser.getCurrentUser(this,Person.class));
                    team.setName(myteam_name);
                    team.setMotto(myteam_slogan);
                    team.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MyTeamActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            et_team_name.setText(team.getName());
                            et_team_slogan.setText(team.getMotto());
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i("result", "~~~~~~~~~~~~~~~~~~~~~~~~~~~" + s);
                            Toast.makeText(MyTeamActivity.this, "保存失败" + s, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                break;
            case R.id.btn_team_addmemember:

                break;
        }
    }
}
