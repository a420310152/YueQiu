package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.CommunityAdapter;
import com.jhy.org.yueqiu.adapter.CommunityCommentAdapter;
import com.jhy.org.yueqiu.domain.Comment;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.view.CommunityReleaseLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import static com.jhy.org.yueqiu.R.drawable.icon_community_commend_red;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityItemActivity extends Activity{
    private Post post;
    private Context context = this;
    private Button btn_community_zf;//浏览量按钮
    private Button btn_community_comment;//评论按钮
    private Button btn_community_commend;//赞按钮
    private TextView tv_comment;
    private ListView lv_item_comment;
    private CommunityCommentAdapter commentAdapter;
    private CommunityReleaseLayout communityReleaseLayout;
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_item);
        init();
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        //int i = 0;
        //btn_community_zf.setText(i+1);
        setPostInfo(post);
        setComment(post);
    }
    private void init(){
        communityReleaseLayout = (CommunityReleaseLayout) findViewById(R.id.communityreleaseLayout);
        btn_community_zf = (Button) findViewById(R.id.btn_community_zf);
        btn_community_comment = (Button) findViewById(R.id.btn_community_comment);
        btn_community_commend = (Button) findViewById(R.id.btn_community_commend);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        lv_item_comment = (ListView) findViewById(R.id.lv_item_comment);
        //评论按钮监听
        btn_community_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(CommunityItemActivity.this, CommunityComment.class);
                commentIntent.putExtra("post",post);
                startActivity(commentIntent);
            }
        });
        //赞按钮监听
        btn_community_commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(flag == false) {
                Drawable drawable = getResources().getDrawable(R.drawable.icon_community_commend_red);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                btn_community_commend.setCompoundDrawables(drawable, null, null, null);
                flag = true;
            }else if(flag == true){
                Drawable drawable = getResources().getDrawable(R.drawable.icon_community_commend);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                btn_community_commend.setCompoundDrawables(drawable, null, null, null);
                flag = false;
            }
            }
        });
    }
    private void setPostInfo(Post post){
        Picasso.with(context).load(post.getAvatarUrl()).transform(new RoundTransform()).into(communityReleaseLayout.getiv_community_head());
        communityReleaseLayout.gettv_community_userName().setText(post.getAuthor().getUsername());
        communityReleaseLayout.gettv_community_buildtime().setText(post.getCreatedAt());
        communityReleaseLayout.gettv_community_info().setText(post.getContent());
        communityReleaseLayout.getbtn_commend().setVisibility(View.GONE);
        communityReleaseLayout.getbtn_comment().setVisibility(View.GONE);
        communityReleaseLayout.getbtn_scan().setVisibility(View.GONE);
    }
    private void setComment(Post post){
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("post", new BmobPointer(post));
        Log.e("setComment", "查询到的评论的帖子" + post);
        query.include("commenter,post.author");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                commentAdapter = new CommunityCommentAdapter(context,list);
                lv_item_comment.setAdapter(commentAdapter);
                Log.e("setComment", "查询到的评论" + list.size());
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
