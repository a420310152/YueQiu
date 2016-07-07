package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.bmob.v3.BmobUser;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class MyFootprintActivity extends Activity {
    private Context context = this;
    private BmobUser my_footprint_bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint);
        judgeLogin();
    }
    //判断登录状态
    private void judgeLogin(){
        my_footprint_bmobUser = BmobUser.getCurrentUser(context);
        if(my_footprint_bmobUser!=null){

        }else{
            startActivity(new Intent(MyFootprintActivity.this,LoginActivity.class));
            finish();
        }
    }
}
