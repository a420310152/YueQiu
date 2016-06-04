package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Comment;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.ActionBarLayout;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**;
 * Created by Administrator on 2016/5/17.
 */
public class CommunityComment extends Activity{
    private ActionBarLayout actionBarLayout;
    private EditText et_community_comment;
    private Context context = this;
    private Person person;
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_comment);
        init();
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
    }
    private void init(){
        actionBarLayout = (ActionBarLayout) findViewById(R.id.actionbar_community_comment);
        et_community_comment = (EditText) findViewById(R.id.et_community_comment);
        person = BmobUser.getCurrentUser(context,Person.class);
        actionBarLayout.setOptionsOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });
    }
    private void saveComment(){
        String commentInfo = et_community_comment.getText().toString();
        if(Utils.isEmpty(commentInfo)){
            Toast.makeText(context,"请填写评论内容",Toast.LENGTH_SHORT).show();
        }else{
            Comment comment = new Comment();
            comment.setText(commentInfo);
            comment.setCommenter(person);
            comment.setPost(post);
            comment.setAvatarUrl(person.getAvatarUrl());
            comment.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }
}
