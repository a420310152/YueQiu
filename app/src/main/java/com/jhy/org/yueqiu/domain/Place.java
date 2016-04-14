package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class Place extends BmobObject {
    private String name;            // 名字
    private BmobFile image;         // 图片
    private BmobGeoPoint location;  // 位置

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }
}
