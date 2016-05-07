package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AddTeam extends BmobObject{
    private Person member;//成员
    private Team addTeam;//加入的球队

    public Person getMember() {
        return member;
    }

    public void setMember(Person member) {
        this.member = member;
    }

    public Team getAddTeam() {
        return addTeam;
    }

    public void setAddTeam(Team addTeam) {
        this.addTeam = addTeam;
    }
}
