package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener ;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.CommunityAdapter;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.ActionBarLayout;
import com.jhy.org.yueqiu.view.CommunityPostLayout;
import com.jhy.org.yueqiu.view.CommunityReleaseLayout;
import com.jhy.org.yueqiu.view.LoadingImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/5/11.
 */
public class CommunityActivity extends Activity{
    private ActionBarLayout actionBarLayout;
    private CommunityPostLayout communityPostLayout;
    private ListView lv_community_post;
    private CommunityAdapter communityAdapter;
    private BmobUser currentUser;
    private Context context = this;
    private boolean switcher = false;
    private Intent postIntent;
    private Post post;
    private List<Post> postList = new ArrayList<>();//所有帖子的集合
    private LoadingImageView my_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        //判断登录状态
        currentUser = Person.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(CommunityActivity.this, LoginActivity.class));
            finish();
        } else {
            init();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        my_loading.show();
        setCommunityInfo();
    }

    //初始化控件
    private void init(){
        actionBarLayout = (ActionBarLayout) findViewById(R.id.actionbar_community_title);
        communityPostLayout = (CommunityPostLayout) findViewById(R.id.community_post);
        lv_community_post = (ListView) findViewById(R.id.lv_community_post);
        my_loading = (LoadingImageView) findViewById(R.id.my_loading);
        actionBarLayout.getOptionsView().setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               if (switcher == false) {
                   switcher = true;
                   communityPostLayout.setVisibility(View.VISIBLE);
               } else {
                   communityPostLayout.setVisibility(View.INVISIBLE);
                   switcher = false;
               }
           }
       });
        lv_community_post.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postIntent = new Intent(CommunityActivity.this, CommunityItemActivity.class);
                post = postList.get(position);
                postIntent.putExtra("post", post);
                Log.e("listView", "传递的post" + post);
                startActivity(postIntent);
            }
        });
        communityPostLayout.getbuttonView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                communityPostLayout.sendPost();
            }
        });
    }
    //查询数据
    public void setCommunityInfo(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("title,content,author");
        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                postList.clear();
                postList.addAll(list);
                Log.e("setCommunityInfo", "发布的数据" + postList);
                communityAdapter = new CommunityAdapter(context, postList);
                lv_community_post.setAdapter(communityAdapter);
                my_loading.hide();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
