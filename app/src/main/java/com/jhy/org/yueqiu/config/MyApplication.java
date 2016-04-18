package com.jhy.org.yueqiu.config;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.jhy.org.yueqiu.test.h.OnReceiveUserLocationListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class MyApplication extends Application implements BDLocationListener {
    private static LatLng userLocation = null;
    private static LocationClient locationClient = null;
    private static List<OnReceiveUserLocationListener> locationListeners = null;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Bmob.initialize(this, Key.bmob.application_id);

        this.locationListeners = new ArrayList<OnReceiveUserLocationListener>();

        initLocation();
        locationClient.start();
    }

    public static void registerReceiveUserLocation (OnReceiveUserLocationListener listener) {
        if (userLocation == null) {
            locationListeners.add(listener);
        } else {
            listener.onReceiveUserLocation(userLocation);
        }
    }

    private void initLocation () {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        this.locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        locationClient.setLocOption(option);
//        Log.i("ilog", "准备定位");
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        userLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//        Log.i("ilog:", "定位成功---(" + userLocation.latitude + ", " + userLocation.longitude + ")");
        locationClient.stop();
        for (OnReceiveUserLocationListener listener : locationListeners) {
            listener.onReceiveUserLocation(userLocation);
        }
    }
}