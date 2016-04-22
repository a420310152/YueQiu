package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.ApplyAdapter;
import com.jhy.org.yueqiu.domain.Challenge;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyApplyActivity extends Activity{
    private ApplyAdapter applyAdapter;
    private List<Challenge> list;
    private ListView lv_apply_info;
    private BmobUser my_apply_bmobuser;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
        lv_apply_info = (ListView) findViewById(R.id.lv_apply_info);
        list = new ArrayList<Challenge>();
        applyAdapter = new ApplyAdapter(MyApplyActivity.this,list);
        Log.i("result","!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~!");
        lv_apply_info.setAdapter(applyAdapter);
        judgeLogin();
    }
    //判断登录状态
    private void judgeLogin(){
        my_apply_bmobuser = BmobUser.getCurrentUser(context);
        if(my_apply_bmobuser!=null){
            lv_apply_info.setOnItemClickListener(click);
        }else{
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }
    AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };


    //返回按钮的监听
    public void myApplyBackClick(View v){
        finish();
    }
}
