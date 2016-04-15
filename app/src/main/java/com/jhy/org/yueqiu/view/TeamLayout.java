package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Team;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class TeamLayout extends RelativeLayout{

    ImageView iv_team_logo;
    TextView tv_team_name;
    TextView tv_team_number;
    TextView tv_team_time;
    public TeamLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_team,this);
        this.iv_team_logo = (ImageView) findViewById(R.id.iv_team_logo);
        this.tv_team_name = (TextView) findViewById(R.id.tv_team_name);
        this.tv_team_number = (TextView) findViewById(R.id.tv_team_number);
        this.tv_team_time = (TextView) findViewById(R.id.tv_team_time);
    }
    public void setTeam(Team team){
        this.tv_team_name.setText(team.getName());
        this.tv_team_number.setText(team.getMembers().getObjects().size());
        this.tv_team_time.setText(team.getCreatedAt());
    }

}
