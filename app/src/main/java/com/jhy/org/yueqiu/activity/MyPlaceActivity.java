package com.jhy.org.yueqiu.activity;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.PlaceAdapter;
import com.jhy.org.yueqiu.bmob.BmobUtils;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.OnReceiveUserLocationListener;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.MyPlace;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.BaiduMapLayout;
import com.jhy.org.yueqiu.view.LoadingImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class MyPlaceActivity extends Activity implements OnGetPoiSearchResultListener, AdapterView.OnItemClickListener, OnReceiveUserLocationListener {
    private Context context = this;
    private Person currentUser = null;
    private BmobUser myPlace_bmobUser;
    private LatLng userLocation;
    private List<String> userCollection;
    private List<MyPlace> placeList = new ArrayList<>();
    private PlaceAdapter placeAdapter;
    private View selectedView = null;
    private ListView lv_places;
    private LoadingImageView my_loading;
    private int resultCount = 0;

    private BaiduMapLayout my_baiduMap;
    private PoiSearch poiSearch;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (userLocation != null) {
                placeAdapter = new PlaceAdapter(context, placeList, userCollection, userLocation);
                lv_places.setAdapter(placeAdapter);
            }
            my_loading.hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_place);
        currentUser = BmobUtils.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MyPlaceActivity.this, LoginActivity.class));
            finish();
        } else {
            initView();
            App.registerReceiveUserLocation(this);
            queryCollection();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        my_loading.show();
    }

    private void initView () {
        lv_places = (ListView) findViewById(R.id.lv_places);
        lv_places.setOnItemClickListener(this);

        my_loading = (LoadingImageView) findViewById(R.id.my_loading);

        my_baiduMap = (BaiduMapLayout) findViewById(R.id.my_baiduMap);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    // 1. 先查询到用户收藏的地点
    private void queryCollection () {
        if (currentUser != null) {
            BmobQuery<Person> query = new BmobQuery<>();
            query.getObject(context, currentUser.getObjectId(), new GetListener<Person>() {
                @Override
                public void onSuccess(Person person) {
                    userCollection = person.getCollection();
                    searchNext();
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    private void searchNext () {
        if (!Utils.isEmpty(userCollection) && resultCount < userCollection.size()) {
            PoiDetailSearchOption option = new PoiDetailSearchOption();
            option.poiUid(userCollection.get(resultCount));
            poiSearch.searchPoiDetail(option);
            resultCount += 1;
        } else {
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    // 2. 将查询到地点保存起来
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.i("ilog", "检索失败: " + poiDetailResult.error.toString());
        } else {
            Log.i("ilog", "查询到的详细地址: " + poiDetailResult.getUid());
            placeList.add(new MyPlace(poiDetailResult));
        }
        if (resultCount == userCollection.size()) {
            handler.sendEmptyMessage(1);
        }
        searchNext();
    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        this.userLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyPlace info = placeList.get(position);
        my_baiduMap.setTitle(info.name);
        my_baiduMap.setPosition(info.location);
        my_baiduMap.setVisibility(View.VISIBLE);

        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }
        view.setBackgroundColor(0x99AABBCC);
        selectedView = view;
    }
}
