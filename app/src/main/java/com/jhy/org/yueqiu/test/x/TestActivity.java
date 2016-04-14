package com.jhy.org.yueqiu.test.x;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.fragment.HomeFragment;

/**
 * Created by Administrator on 2016/4/12.
 */
public class TestActivity extends FragmentActivity{
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        build();
    }
    private void build(){
        manager = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.linear,fragment);
        ft.commit();
    }

}
