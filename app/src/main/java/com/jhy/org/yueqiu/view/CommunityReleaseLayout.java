package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityReleaseLayout extends RelativeLayout{
    private ImageView iv_community_head;//头像
    private TextView tv_community_userName;//标题
    private TextView tv_community_info;//内容
    private TextView tv_community_buildtime;//时间
    private TextView tv_community_scan;//浏览量数目
    private Button btn_comment;//评论按钮
    private TextView tv_community_comment;//评论量数目
    private Button btn_commend;//赞按钮
    private Context context;
    private Post post;
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
        btn_comment = (Button) findViewById(R.id.btn_comment);
        btn_commend = (Button) findViewById(R.id.btn_commend);
        tv_community_buildtime = (TextView) findViewById(R.id.tv_community_buildtime);
    }
    public void setCommunityInfo(Post post){
        this.post = post;
        Picasso.with(context).load(post.getAvatarUrl()).transform(new RoundTransform()).into(iv_community_head);
        tv_community_userName.setText(post.getAuthor().getUsername());
        tv_community_buildtime.setText(post.getCreatedAt());
        tv_community_info.setText(post.getContent());
    }
}
