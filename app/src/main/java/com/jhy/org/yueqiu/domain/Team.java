package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class Team extends BmobObject {
    private String name;            // 名称
    private BmobFile logo;          // Logo标志
    private Person creator;         // 创建者
    private BmobRelation members;   // 队员
    private String motto;           // 口号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getLogo() {
        return logo;
    }

    public void setLogo(BmobFile logo) {
        this.logo = logo;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public BmobRelation getMembers() {
        return members;
    }

    public void setMembers(BmobRelation members) {
        this.members = members;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
