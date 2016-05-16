package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class SettingActivity extends Activity implements OnClickListener {
    private TextView tv_system_set_cancle;
    private TextView tv_system_set_toast;
    private TextView tv_system_message_set;
    private BmobUser setting_bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        judgeLogin();
    }
    //判断登录状态
    private void judgeLogin() {
        setting_bmobUser = BmobUser.getCurrentUser(this);
        if (setting_bmobUser != null) {

        } else {
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            finish();
        }
    }
    //初始化控件
    private void init(){
        tv_system_set_cancle = (TextView) findViewById(R.id.tv_system_set_cancle);
        tv_system_set_toast = (TextView) findViewById(R.id.tv_system_set_toast);
        tv_system_message_set = (TextView) findViewById(R.id.tv_system_message_set);
        tv_system_set_toast.setOnClickListener(this);
        tv_system_message_set.setOnClickListener(this);
        tv_system_set_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_system_set_cancle:
                logout();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.tv_system_set_toast:
                Log.i("result","!!!!!!!!!!!!!!53125353643462!!!!!!!!!!!");
                Intent intentToast = new Intent(SettingActivity.this,SetttingToastActivity.class);
                startActivity(intentToast);
                break;
            case R.id.tv_system_message_set:
                Intent intentMessage = new Intent(SettingActivity.this,SettingMessageActivity.class);
                startActivity(intentMessage);
                break;
        }
    }
    private void logout () {
        BmobUser.logOut(this);   //清除缓存用户对象
    }
}
