package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Team;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AllTeamLayout extends RelativeLayout{
    private ImageView iv_allteam_head;//球队logo
    private TextView tv_selector_allteam_name;//球队名字
    private TextView tv_team_buildname;//球队人数
    private TextView tv_selector_allteam_time;//球队成立时间
    private TextView tv_allteam_slogan;//球队宣言
    private Context context ;
    private Team team;
    public AllTeamLayout(Context context) {
        super(context);
        init(context);
    }
    public AllTeamLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AllTeamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_allteam, this);
        iv_allteam_head = (ImageView) findViewById(R.id.iv_allteam_head);
        tv_selector_allteam_name = (TextView) findViewById(R.id.tv_selector_allteam_name);
        tv_team_buildname = (TextView) findViewById(R.id.tv_team_buildname);
        tv_selector_allteam_time = (TextView) findViewById(R.id.tv_selector_allteam_time);
        tv_allteam_slogan = (TextView) findViewById(R.id.tv_allteam_slogan);
    }
    public void setAllTeamInfo(Team team){
        this.team = team;
        BmobQuery<Team> teamQuery = new BmobQuery<Team>();
        teamQuery.getObject(context, team.getCreator().getObjectId(), new GetListener<Team>() {
            @Override
            public void onSuccess(Team team) {

                tv_selector_allteam_name.setText(team.getName());
                tv_team_buildname.setText(team.getCreator()+"");
                tv_allteam_slogan.setText(team.getMotto());
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
