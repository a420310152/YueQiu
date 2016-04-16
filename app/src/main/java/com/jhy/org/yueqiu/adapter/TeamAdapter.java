package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.view.TeamLayout;

import java.util.List;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class TeamAdapter extends MyBaseAdapter<Team>{
    TeamLayout teamLayout;
    Team team;
    public TeamAdapter(Context context, List<Team> list) {
            super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        team =  list.get(position);
        teamLayout.setTeam(team);
        return convertView;
    }

}
