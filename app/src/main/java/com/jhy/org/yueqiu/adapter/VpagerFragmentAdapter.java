package com.jhy.org.yueqiu.adapter;
/*
 **********************************************
 * 			所有者 X: (夏旺)
 * 		此类暂时遗弃
 **********************************************
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class VpagerFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    public VpagerFragmentAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list = list;
    }
    public float getPageWidth(int position){
        if (position==0){
            return 0.4f;
        }
        return 1f;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
}
