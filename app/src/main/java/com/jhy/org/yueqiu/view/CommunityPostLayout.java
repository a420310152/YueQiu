package com.jhy.org.yueqiu.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.okhttp.internal.Util;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityPostLayout extends RelativeLayout{
    private EditText et_community_title;
    private EditText et_community_info;
    private Button btn_community_send;
    private String title;
    private String info;
    private Person person;
    private Context context;


    public CommunityPostLayout(Context context) {
        super(context);
    }

    public CommunityPostLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_community_post, this);
        et_community_title = (EditText) findViewById(R.id.et_community_title);
        et_community_info = (EditText) findViewById(R.id.et_community_info);
        btn_community_send = (Button) findViewById(R.id.btn_community_send);
        person = BmobUser.getCurrentUser(context,Person.class);
    }
    //上传帖子
    public void sendPost() {
        title = et_community_title.getText().toString();
        info = et_community_info.getText().toString();
        if (Utils.isEmpty(title)) {
            Toast.makeText(context, "请输入标题", Toast.LENGTH_SHORT).show();
        } else if (Utils.isEmpty(info)) {
            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
        } else {
            Post post = new Post();
            post.setTitle(title);
            post.setContent(info);
            post.setAuthor(person);
            post.setAvatarUrl(person.getAvatarUrl());
            post.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.e("sendPost", "上传贴子成功");
                    if(!Utils.isEmpty(title) && !Utils.isEmpty(info)){
                        setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onFailure(int i, String s) {
                    Log.e("sendPost", "上传贴子失败" + s);
                }
            });
        }
    }
    public Button getbuttonView () {
        return btn_community_send;
    }
    public EditText getTitleView () {
        return et_community_title;
    }
    public EditText getInfoView () {
        return et_community_info;
    }
    //回调刷新
    public static interface InotifyInfo{
        void setCommunityInfo();
    }



}
