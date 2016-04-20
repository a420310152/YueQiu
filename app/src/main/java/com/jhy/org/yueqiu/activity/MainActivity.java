package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, Key.bmob.application_id);
        build();

    }

    private void build() {
    viewPager = (ViewPager) findViewById(R.id.vPager);
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new SidebarFragment());
        list.add(new HomeFragment());
        VpagerFragmentAdapter adapter = new VpagerFragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        viewPager.setOnPageChangeListener(clickVpager);
    }
    public void loginMenu(View v){
        Log.i("loginMenu","loginMenu===="+viewPager.getCurrentItem());
        if (viewPager.getCurrentItem()==1){
            viewPager.setCurrentItem(0);
        }else if (viewPager.getCurrentItem()==0){
            viewPager.setCurrentItem(1);
        }
    }

    //后期：可以对滑动监听进行设置***(暂时没用)
    ViewPager.OnPageChangeListener clickVpager = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
