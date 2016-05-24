package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.AddTeam;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AllTeamLayout extends RelativeLayout{
    private ImageView iv_allteam_head;//球队logo
    private TextView tv_selector_allteam_name;//球队名字
    private TextView tv_team_buildname;//创建者
    private TextView tv_selector_allteam_time;//球队成立时间
    private TextView tv_allteam_slogan;//球队宣言
    private Context context ;
    private Person person;
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
        person = BmobUser.getCurrentUser(context, Person.class);
    }
    //查询我创建球队的信息并设置
    public void setAllTeamInfo(Team buildTeam){
        tv_selector_allteam_name.setText(buildTeam.getName());
        tv_team_buildname.setText(person.getUsername());
        tv_selector_allteam_time.setText(buildTeam.getCreatedAt());
        tv_allteam_slogan.setText(buildTeam.getMotto());
        String logo = buildTeam.getLogoUrl();
        Picasso.with(context)
                .load(logo)
                .transform(new RoundTransform())
                .into(iv_allteam_head);
    }

     //设置我加入球队的信息        
    public void setAddTeam(Team addTeam) {
        tv_selector_allteam_name.setText(addTeam.getName());
        tv_team_buildname.setText(addTeam.getCreator().getUsername());
        tv_selector_allteam_time.setText(addTeam.getCreatedAt());
        tv_allteam_slogan.setText(addTeam.getMotto());
        String logo = addTeam.getLogoUrl();
        Picasso.with(context)
                .load(logo)
                .transform(new RoundTransform())
                .into(iv_allteam_head);
    }
}