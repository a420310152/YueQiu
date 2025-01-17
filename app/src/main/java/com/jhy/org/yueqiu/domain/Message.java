package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class Message extends BmobObject {
    private Person source;          // 发送者
    private Person target;          // 接受者
    private String text;            // 文本内容
    private BmobFile image;         // 图片内容

    public Person getSource() {
        return source;
    }

    public void setSource(Person source) {
        this.source = source;
    }

    public Person getTarget() {
        return target;
    }

    public void setTarget(Person target) {
        this.target = target;
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
