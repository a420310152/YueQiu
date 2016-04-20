package com.jhy.org.yueqiu.test.c;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.jhy.org.yueqiu.activity.LoginActivity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.fragment.SidebarFragment;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TestActivity extends FragmentActivity {

    FragmentManager fm_sidebar;
    FragmentTransaction ft_sidebar;
    SidebarFragment sf_fragment;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Bmob.initialize(this, Key.bmob.application_id);
    }


    public void init (){
        fm_sidebar = getSupportFragmentManager();
        ft_sidebar = fm_sidebar.beginTransaction();
        sf_fragment = new SidebarFragment();
        sf_fragment.setContext(this);
        sf_fragment.judge();
        ft_sidebar.add(R.id.linear, sf_fragment);
        ft_sidebar.commit();
    }



}
