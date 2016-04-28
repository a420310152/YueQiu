package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.view.TeamLayout;

import java.util.List;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class TeamAdapter extends MyBaseAdapter<Person>{
    public TeamAdapter(Context context, List<Person> list) {
            super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamLayout view = (TeamLayout)convertView;
        if (view == null) {
            view  = new TeamLayout(context);
        }
        view.setTeam(list.get(position));
        return view;
    }

}
