package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class MyBaseAdapter<T> extends BaseAdapter{
    public List<T> list;
    public Context context;
    public LayoutInflater inflater;

    public MyBaseAdapter(List<T> list,Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list==null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }

    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
}
