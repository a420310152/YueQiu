package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityItemActivity extends Activity{

    private Post post;
    private Context context = this;
    private ImageView iv_community_item_head;
    private TextView tv_community_item_userName;
    private  TextView tv_community_item_info;
    private TextView tv_community_item_buildtime;
    private ListView lv_item_commend;//评论的下拉列表
    private Button btn_community_zf;//转发按钮
    private Button btn_community_comment;//评论按钮
    private Button btn_community_commend;//赞按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_item);
        init();
        setPostInfo(post);
    }
    private void init(){
        iv_community_item_head = (ImageView) findViewById(R.id.iv_community_item_head);
        tv_community_item_userName = (TextView) findViewById(R.id.tv_community_item_userName);
        tv_community_item_info = (TextView) findViewById(R.id.tv_community_item_info);
        tv_community_item_buildtime = (TextView) findViewById(R.id.tv_community_item_buildtime);
        lv_item_commend = (ListView) findViewById(R.id.lv_item_commend);
        btn_community_zf = (Button) findViewById(R.id.btn_community_zf);
        btn_community_comment = (Button) findViewById(R.id.btn_community_comment);
        btn_community_commend = (Button) findViewById(R.id.btn_community_commend);
    }
    private void setPostInfo(Post post){
        this.post = post;
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        Picasso.with(context).load(post.getAvatarUrl()).transform(new RoundTransform()).into(iv_community_item_head);
        tv_community_item_userName.setText(post.getAuthor().getUsername());
        tv_community_item_buildtime.setText(post.getCreatedAt());
        tv_community_item_info.setText(post.getContent());
    }
}
