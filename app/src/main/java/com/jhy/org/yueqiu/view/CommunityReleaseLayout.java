package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.CommunityComment;
import com.jhy.org.yueqiu.domain.Comment;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityReleaseLayout extends RelativeLayout{
    private ImageView iv_community_head;//头像
    private TextView tv_community_userName;//标题
    private TextView tv_community_info;//内容
    private TextView tv_community_buildtime;//时间
    private Button btn_scan;//浏览量按钮
    private Button btn_comment;//评论按钮
    private Button btn_commend;//赞按钮
    private Context context;
    private Post post;
    Comment comment;
    private Person person;
    public CommunityReleaseLayout(Context context) {
        super(context);
        init(context);
    }

    public CommunityReleaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CommunityReleaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    private void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_community_release, this);
        person = BmobUser.getCurrentUser(context,Person.class);
        iv_community_head = (ImageView) findViewById(R.id.iv_community_head);
        tv_community_userName = (TextView) findViewById(R.id.tv_community_userName);
        tv_community_info = (TextView) findViewById(R.id.tv_community_info);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_comment = (Button) findViewById(R.id.btn_comment);
        btn_commend = (Button) findViewById(R.id.btn_commend);
        tv_community_buildtime = (TextView) findViewById(R.id.tv_community_buildtime);
    }
    //设置帖子信息
    public void setCommunityInfo(Post post){
        this.post = post;
        Picasso.with(context).load(post.getAvatarUrl()).transform(new RoundTransform()).into(iv_community_head);
        tv_community_userName.setText(post.getAuthor().getUsername());
        tv_community_buildtime.setText(post.getCreatedAt());
        tv_community_info.setText(post.getContent());
    }
    //设置评论者信息
    public void setCommentInfo(Comment comment){
        Log.e("setCommentInfo", "查询的comment" + comment);
        if (comment != null) {
            Picasso.with(context).load(comment.getAvatarUrl()).transform(new RoundTransform()).into(iv_community_head);
            tv_community_userName.setText(comment.getCommenter().getUsername());
            tv_community_buildtime.setText(comment.getCreatedAt());
            tv_community_info.setText(comment.getText());
            btn_scan.setVisibility(View.GONE);
            btn_comment.setVisibility(View.GONE);
            btn_commend.setVisibility(View.GONE);
        }
    }
    public ImageView  getiv_community_head(){
        return iv_community_head;
    }
    public TextView  gettv_community_userName(){
        return tv_community_userName;
    }
    public TextView  gettv_community_info(){
        return tv_community_info;
    }
    public TextView  gettv_community_buildtime(){
        return tv_community_buildtime;
    }
    public Button  getbtn_scan(){
        return btn_scan;
    }
    public Button  getbtn_comment(){
        return btn_comment;
    }
    public Button  getbtn_commend(){
        return btn_commend;
    }
}
