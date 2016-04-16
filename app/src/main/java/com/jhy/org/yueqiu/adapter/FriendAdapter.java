package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.FriendLayout;

import java.util.List;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class FriendAdapter extends MyBaseAdapter<Person> {
    public FriendAdapter(Context context, List<Person> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendLayout layout = (FriendLayout) convertView;
        if (layout == null) {
            layout = new FriendLayout(context);
        }
        layout.setPerson(list.get(position));
        return layout;
    }
}
