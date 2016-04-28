package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.view.AllTeamLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AllTeamAdapter extends MyBaseAdapter<Team>{

    private AllTeamLayout allTeamLayout;
    private Team team;
    public AllTeamAdapter(Context context, List<Team> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        team = list.get(position);
        AllTeamLayout view = (AllTeamLayout) convertView;
        if(allTeamLayout!=null) {
            allTeamLayout.setAllTeamInfo(team);
        }
        return convertView;
    }
}
