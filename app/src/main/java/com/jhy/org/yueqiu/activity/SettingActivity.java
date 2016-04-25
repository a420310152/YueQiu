package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    TextView tv_system_set_toast;
    TextView tv_system_message_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }
    private void init(){
        tv_system_set_toast = (TextView) findViewById(R.id.tv_system_set_toast);
        tv_system_message_set = (TextView) findViewById(R.id.tv_system_message_set);
        tv_system_set_toast.setOnClickListener(this);
        tv_system_message_set.setOnClickListener(this);
    }
    public void setSystemBackClick(View v){
            finish();
        }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
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
}
