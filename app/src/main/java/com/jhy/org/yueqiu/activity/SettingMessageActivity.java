package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/23.
 */
public class SettingMessageActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
    }
    public void settingMessageBackClick(View v){
        finish();
    }
}
