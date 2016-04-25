package com.jhy.org.yueqiu.test.h.backups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.MyProfileActivity;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.config.MyApplication;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.test.h.OnReceiveUserLocationListener;

import java.util.ArrayList;
import java.util.List;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchFriendActivity extends Activity implements AdapterView.OnItemClickListener, RadarSearchListener, OnReceiveUserLocationListener {
    private Context context = this;
    private Intent myProfileIntent;

    private ListView lv_friends;
    private List<Person> friendList;
    private FriendAdapter friendAdapter;

    private RadarSearchManager searchManager;
    private RadarNearbySearchOption searchOption;
    private Person currentUser = null;
    private LatLng userLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        this.lv_friends = (ListView) findViewById(R.id.lv_friends);
        this.friendList = new ArrayList<Person>();
        this.friendAdapter = new FriendAdapter(context, friendList);
        lv_friends.setOnItemClickListener(this);

        this.myProfileIntent = new Intent(context, MyProfileActivity.class);

        this.searchManager = RadarSearchManager.getInstance();
        this.searchOption = new RadarNearbySearchOption()
                .pageNum(0)
                .radius(10000);
        MyApplication.registerReceiveUserLocation(this);
        searchManager.addNearbyInfoListener(this);//周边雷达设置监听
        // searchManager.startUploadAuto(this, 5000); //设置自动上传的callback和时间间隔
        // searchManager.setUserID(userID); //周边雷达设置用户身份标识，id为空默认是设备标识
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = friendList.get(position);
        myProfileIntent.putExtra("who", person);
        startActivity(myProfileIntent);
    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
        if (radarSearchError != RadarSearchError.RADAR_AK_ERROR.RADAR_NO_ERROR) {
            Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
            return;
        }
//        Log.i("ilog", "查询成功");
        for (RadarNearbyInfo info : radarNearbyResult.infoList) {
//            Log.i("ilog:", "comments: " + info.comments + ", distance: " + info.distance + ", userID: "+ info.userID);
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {

    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {

    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        this.userLocation = location;

        //上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "用户备注信息";
        info.pt = location;
        searchManager.uploadInfoRequest(info); // 发起上传位置请求

        // 周边位置信息检索
        searchOption.centerPt(location);
        searchManager.nearbyInfoRequest(searchOption); // 发起查询请求
    }
}
