package com.jhy.org.yueqiu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.PlaceAdapter;
import com.jhy.org.yueqiu.config.MyApplication;
import com.jhy.org.yueqiu.test.h.BaiduMapLayout;
import com.jhy.org.yueqiu.test.h.OnReceiveUserLocationListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchPlaceActivity extends Activity implements OnGetPoiSearchResultListener, OnReceiveUserLocationListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private PoiSearch poiSearch;
    private PoiNearbySearchOption searchOption;

    private List<PoiInfo> placeList;
    private PlaceAdapter placeAdapter;
    private ListView lv_places;

    private Button btn_ok;
    private int selectedPostion = -1;
    private View selectedView = null;

    private BaiduMapLayout baiduMap;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_places.setAdapter(placeAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        this.poiSearch = PoiSearch.newInstance();
        this.searchOption = new PoiNearbySearchOption()
                .radius(10000)
                .keyword("篮球场")
                .pageNum(0);
        this.placeList = new ArrayList<PoiInfo>();
        this.placeAdapter = new PlaceAdapter(this, placeList);
        this.lv_places = (ListView) findViewById(R.id.lv_places);
        this.btn_ok = (Button) findViewById(R.id.btn_ok);
        this.baiduMap = (BaiduMapLayout) findViewById(R.id.baiduMap);

        poiSearch.setOnGetPoiSearchResultListener(this);
        lv_places.setOnItemClickListener(this);
        MyApplication.registerReceiveUserLocation(this);

        Intent intent = getIntent();
        btn_ok.setVisibility(intent != null && intent.hasExtra("needsPlace") ? View.VISIBLE : View.INVISIBLE);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        placeAdapter.setUserLocation(location);
        searchOption.location(location);
        poiSearch.searchNearby(searchOption);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        this.placeList.addAll(poiResult.getAllPoi());
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    public void finish (View view) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiInfo info = placeList.get(position);
        baiduMap.setTitle(info.name);
        baiduMap.setPosition(info.location);
        baiduMap.setVisibility(View.VISIBLE);

        selectedPostion = position;
        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }
        view.setBackgroundColor(0x99AABBCC);
        selectedView = view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            if (btn_ok.getVisibility() == View.VISIBLE && selectedPostion != -1) {
                Intent intent = new Intent();
                intent.putExtra("place", placeList.get(selectedPostion));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
