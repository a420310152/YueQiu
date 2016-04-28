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
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.OnReceiveUserLocationListener;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.MyPlace;
import com.jhy.org.yueqiu.view.BaiduMapLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
public class MyPlaceActivity extends Activity implements View.OnClickListener, OnGetPoiSearchResultListener, OnReceiveUserLocationListener {
    private Context context = this;
    private Person currentUser = null;

    private LatLng userLocation;
    private List<String> userCollection;
    private List<MyPlace> placeList;
    private PlaceAdapter placeAdapter;
    private ListView lv_places;
    private int resultCount = 0;

    private ImageButton ibtn_back;

    private BaiduMapLayout baiduMap;
    private PoiSearch poiSearch;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            placeAdapter = new PlaceAdapter(context, placeList, userCollection, userLocation);
            lv_places.setAdapter(placeAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_place);

        currentUser = BmobUser.getCurrentUser(context, Person.class);
        App.registerReceiveUserLocation(this);

        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);

        placeList = new ArrayList<>();
        lv_places = (ListView) findViewById(R.id.lv_places);

        baiduMap = (BaiduMapLayout) findViewById(R.id.baiduMap);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        queryCollection();
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
        if (userCollection != null && resultCount < userCollection.size()) {
            PoiDetailSearchOption option = new PoiDetailSearchOption();
            option.poiUid(userCollection.get(resultCount));
            poiSearch.searchPoiDetail(option);
            resultCount += 1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            default:
                break;
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
        baiduMap.setLocation(userLocation);
    }
}
