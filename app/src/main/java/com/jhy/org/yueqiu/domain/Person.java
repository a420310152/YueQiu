package com.jhy.org.yueqiu.domain;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class Person extends BmobUser {
    public static final boolean SEX_MAN = true;
    public static final boolean SEX_WOMAN = false;

    public static final String URL_DEFAULT_AVATAR = "http://file.bmob.cn/M03/46/8A/oYYBAFcfUsCAIBiiAAA2_kOOk8E327.jpg";
    public static final String URL_DEFAULT_LOGO = "http://file.bmob.cn/M03/46/8A/oYYBAFcfUsCAIBiiAAA2_kOOk8E327.jpg";

    private Boolean sex;            // 性别
    private Integer age;            // 年龄
    private Integer height;         // 身高
    private Integer weight;         // 体重
    private Integer proficiency;    // 熟练度
    private String address;         // 地址
    private String position;        // 擅长位置
    private String signature;       // 签名
    private String avatarUrl;       // 头像的地址
    private BmobFile avatar;        // 头像
    private BmobRelation friends;   // 好友
    private BmobRelation newFriends;//待添加的好友
    private BmobGeoPoint location;  // 最近的地理位置
    private List<String> collection;// 收藏的地点
    private BmobRelation footprints;// 足迹
    private BmobRelation addTeam;//加入的球队；
    private Challenge challenge;//加入的挑战

    public BmobRelation getAddTeam() {
        return addTeam;
    }

    public void setAddTeam(BmobRelation addTeam) {
        this.addTeam = addTeam;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public BmobRelation getNewFriends() {
        return newFriends;
    }

    public void setNewFriends(BmobRelation newFriends) {
        this.newFriends = newFriends;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getProficiency() {
        return proficiency;
    }

    public void setProficiency(Integer proficiency) {
        this.proficiency = proficiency;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public BmobRelation getFootprints() {
        return footprints;
    }

    public void setFootprints(BmobRelation footprints) {
        this.footprints = footprints;
    }

    public List<String> getCollection() {
        return collection;
    }

    public void setCollection(List<String> collection) {
        this.collection = collection;
    }
}