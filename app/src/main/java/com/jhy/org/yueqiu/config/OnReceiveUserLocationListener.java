package com.jhy.org.yueqiu.config;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

import cn.bmob.v3.datatype.BmobGeoPoint;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public interface OnReceiveUserLocationListener {
    void onReceiveUserLocation (BDLocation userLocation);
}
