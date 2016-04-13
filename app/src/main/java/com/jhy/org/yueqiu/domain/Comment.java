package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class Comment extends BmobObject {
    private Person commenter;       // 评论的人
    private Post post;              // 评论的帖子
    private String text;            // 文本内容
    private BmobFile image;         // 图片内容

    public Person getCommenter() {
        return commenter;
    }

    public void setCommenter(Person commenter) {
        this.commenter = commenter;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
