package com.jhy.org.yueqiu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jhy.org.yueqiu.activity.LoginActivity;
import com.jhy.org.yueqiu.activity.MainActivity;
import com.jhy.org.yueqiu.activity.MyFlaceActivity;
import com.jhy.org.yueqiu.activity.MyFootprintActivity;
import com.jhy.org.yueqiu.activity.MyProfileActivity;
import com.jhy.org.yueqiu.activity.MyTeamActivity;
import com.jhy.org.yueqiu.activity.SettingActivity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;

import cn.bmob.v3.BmobUser;

/*
 **********************************************
 *          所有者 C: (曹昌盛)
 **********************************************
 */
public class SidebarFragment extends Fragment implements OnClickListener {

    private TextView tv_register_login;
    private Button btn_info;
    private Button btn_collect;
    private Button btn_team;
    private Button btn_track;
    private Button btn_disabled;
    private Button btn_cancel;
    private BmobUser login_bmobUser;
    private View view;
    private Context context;
    public void setContext(Context context){
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view= inflater.inflate(R.layout.fragment_sidebar, container, false);
        init();
        judge();
        return  view;
    }
    //初始化控件
    private void init(){
        tv_register_login = (TextView)view.findViewById(R.id.tv_register_login);
        btn_info = (Button) view.findViewById(R.id.btn_info);
        btn_collect = (Button) view.findViewById(R.id.btn_collect);
        btn_team = (Button) view.findViewById(R.id.btn_team);
        btn_track = (Button) view.findViewById(R.id.btn_track);
        btn_disabled = (Button) view.findViewById(R.id.btn_disabled);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        tv_register_login.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        btn_team.setOnClickListener(this);
        btn_track.setOnClickListener(this);
        btn_disabled.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        if(context != null){
            login_bmobUser = BmobUser.getCurrentUser(context);
        }
    }
        public void judge() {
            if (login_bmobUser != null && btn_cancel != null) {
                String username = (String) BmobUser.getObjectByKey(context, "username");
                tv_register_login.setText(username);
                btn_cancel.setVisibility(View.VISIBLE);
            } else if(btn_cancel != null){
                tv_register_login.setVisibility(View.VISIBLE);
                tv_register_login.setText("登录/注册");
                btn_cancel.setVisibility(View.INVISIBLE);

            }
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register_login:
                    if (login_bmobUser != null) {
                        Intent myprofileIntent = new Intent(getActivity(), MyProfileActivity.class);
                        startActivity(myprofileIntent);
                    } else {
                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                break;
            case R.id.btn_info:
                Intent infoIntent =  new Intent(getActivity(),MyProfileActivity.class);
                startActivity(infoIntent);
                break;
            case R.id.btn_collect:
                Intent collectIntent =  new Intent(getActivity(),MyFlaceActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.btn_team:
                Intent teamIntent =  new Intent(getActivity(), MyTeamActivity.class);
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
                startActivity(new Intent(context, MainActivity.class));
                break;
        }
    }
}