package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
    TeamLayout teamLayout;
    Person person;
    public TeamAdapter(Context context, List<Person> list) {
            super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        person =  list.get(position);
        teamLayout.setTeam(person);
        return convertView;
    }

}
