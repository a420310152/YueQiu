package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.VpagerFragmentAdapter;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.fragment.HomeFragment;
import com.jhy.org.yueqiu.fragment.SidebarFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class MainActivity extends FragmentActivity {
    SidebarFragment sidebarFragment;
    DrawerLayout drawerLayout;
    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, Key.bmob.application_id);
        build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sidebarFragment.judge();
        homeFragment.addChallenge();
    }

    private void build() {
        drawerLayout = (DrawerLayout) findViewById(R.id.container);
        //添加Fragment
        sidebarFragment = new SidebarFragment();
        sidebarFragment.setContext(this);
        homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.linear,homeFragment);
        ft.commit();
        FragmentTransaction ftSide = getSupportFragmentManager().beginTransaction();
        ftSide.add(R.id.linearMenu,sidebarFragment);
        ftSide.commit();
        drawerLayout.setDrawerListener(drawerListener);
    }

    //抽屉菜单监听
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    //左上角菜单按钮监听
    public void loginMenu(View v) {
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
    }


}