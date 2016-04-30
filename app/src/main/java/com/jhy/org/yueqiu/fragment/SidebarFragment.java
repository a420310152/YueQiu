package com.jhy.org.yueqiu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.activity.LoginActivity;
import com.jhy.org.yueqiu.activity.MainActivity;
import com.jhy.org.yueqiu.activity.MyAllTeamActivity;
import com.jhy.org.yueqiu.activity.MyApplyActivity;
import com.jhy.org.yueqiu.activity.MyPlaceActivity;
import com.jhy.org.yueqiu.activity.MyFootprintActivity;
import com.jhy.org.yueqiu.activity.MyProfileActivity;
import com.jhy.org.yueqiu.activity.SettingActivity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

import cn.bmob.v3.BmobUser;

/*
 **********************************************
 *          所有者 C: (曹昌盛)
 **********************************************
 */
public class SidebarFragment extends Fragment implements OnClickListener {

    private TextView tv_register_login;
    private Button btn_apply;
    private Button btn_collect;
    private Button btn_team;
    private Button btn_track;
    private Button btn_disabled;
    private Button btn_cancel;
    private BmobUser login_bmobUser;
    private View view;
    private Context context;

    private RelativeLayout container_header;
    private ImageView img_avatar;


    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_sidebar, container, false);
        init();
        judge();
        return  view;
    }
    //初始化控件
    private void init(){
        tv_register_login = (TextView)view.findViewById(R.id.tv_register_login);
        btn_apply = (Button) view.findViewById(R.id.btn_apply);
        btn_collect = (Button) view.findViewById(R.id.btn_collect);
        btn_team = (Button) view.findViewById(R.id.btn_team);
        btn_track = (Button) view.findViewById(R.id.btn_track);
        btn_disabled = (Button) view.findViewById(R.id.btn_disabled);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        tv_register_login.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        btn_team.setOnClickListener(this);
        btn_track.setOnClickListener(this);
        btn_disabled.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //以下是H修改的部分
//        if(context != null){
//            login_bmobUser = BmobUser.getCurrentUser(context);
//        }
        container_header = (RelativeLayout) view.findViewById(R.id.container_header);
        container_header.setOnClickListener(this);
        img_avatar = (ImageView) view.findViewById(R.id.img_avatar);

    }
    public void judge() {
        Person currentUser = BmobUser.getCurrentUser(context, Person.class);

        if (currentUser != null && btn_cancel != null) {
            String username = currentUser.getUsername();
            tv_register_login.setText(username);
            btn_cancel.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(currentUser.getAvatarUrl())
                    .transform(new RoundTransform())
                    .into(img_avatar);
        } else if(btn_cancel != null){
            tv_register_login.setVisibility(View.VISIBLE);
            tv_register_login.setText("登录/注册");
            btn_cancel.setVisibility(View.INVISIBLE);

            img_avatar.setImageResource(R.drawable.icon_sidebar_head);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register_login:
            case R.id.container_header:
                login_bmobUser = BmobUser.getCurrentUser(context);
                if (login_bmobUser != null) {
                    Intent myprofileIntent = new Intent(getActivity(), MyProfileActivity.class);
                    startActivity(myprofileIntent);
                } else {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                }
                break;
            case R.id.btn_apply:
                Intent infoIntent =  new Intent(getActivity(),MyApplyActivity.class);
                startActivity(infoIntent);
                break;
            case R.id.btn_collect:
                Intent collectIntent =  new Intent(getActivity(),MyPlaceActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.btn_team:
                Intent teamIntent =  new Intent(getActivity(), MyAllTeamActivity.class);
                startActivity(teamIntent);
                break;
            case R.id.btn_track:
                Intent trackIntent =  new Intent(getActivity(), MyFootprintActivity.class);
                startActivity(trackIntent);
                break;
            case R.id.btn_disabled:
                Intent settingIntent =  new Intent(getActivity(), SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.btn_cancel:
                BmobUser.logOut(context);   //清除缓存用户对象
                tv_register_login.setText("登录/注册");
                startActivity(new Intent(context, LoginActivity.class));
                break;
        }
    }
}