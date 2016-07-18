package com.jhy.org.yueqiu.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import com.jhy.org.yueqiu.bmob.BmobUtils;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.OnReceiveUserLocationListener;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.MyPlace;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.ActionBarLayout;
import com.jhy.org.yueqiu.view.BaiduMapLayout;
import com.jhy.org.yueqiu.view.LoadingImageView;

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
public class SearchPlaceActivity extends Activity implements OnReceiveUserLocationListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context = this;
    private Person currentUser;
    private PoiSearch poiSearch;
    private PoiNearbySearchOption searchOption;

    private List<MyPlace> placeList = new ArrayList<>();
    private List<String> userCollection;
    private LatLng userLocation;
    private PlaceAdapter placeAdapter;
    private ListView lv_places;
    private boolean readyToSetAdpater = false;

    private LoadingImageView my_loading;
    private TextView tv_title;
    private ImageButton ibtn_yes;

    private int selectedPostion = -1;
    private View selectedView = null;
    private boolean needsPlace = false;

    private BaiduMapLayout baiduMap;

    private static Logx logx = new Logx(SearchPlaceActivity.class);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAdapter(currentUser == null);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        currentUser = BmobUtils.getCurrentUser();

        resolveIntent(getIntent());
        initSearch();
        initView();
        App.registerReceiveUserLocation(this);
        queryCollection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        my_loading.show();
    }

    private void initView () {
        lv_places = (ListView) findViewById(R.id.lv_places);
        lv_places.setOnItemClickListener(this);

        baiduMap = (BaiduMapLayout) findViewById(R.id.baiduMap);
        baiduMap.setVisibility(View.INVISIBLE);

        ActionBarLayout my_actionBar = (ActionBarLayout) findViewById(R.id.my_actionBar);
        tv_title = my_actionBar.getTitleView();
        ibtn_yes = my_actionBar.getOptionsView();
        ibtn_yes.setOnClickListener(this);

        my_loading = (LoadingImageView) findViewById(R.id.my_loading);
    }

    private void initSearch () {
        poiSearch = PoiSearch.newInstance();
        searchOption = new PoiNearbySearchOption()
                .radius(10000)
                .keyword("篮球场")
                .pageNum(0);

        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                for (PoiInfo info : poiResult.getAllPoi()) {
                    placeList.add(new MyPlace(info));
                }
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
    }

    private void resolveIntent (Intent intent) {
        boolean needsPlace = intent.getBooleanExtra("needsPlace", false);
        this.needsPlace = needsPlace;
    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        this.userLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        searchOption.location(this.userLocation);
        poiSearch.searchNearby(searchOption);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //logx.e("点击列表里的某一项[\" + position + \"]");
        MyPlace info = placeList.get(position);
        baiduMap.setTitle(info.name);
        baiduMap.setPosition(info.location);
        baiduMap.setVisibility(View.VISIBLE);

        selectedPostion = position;
        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }
        view.setBackgroundColor(0x99AABBCC);
        selectedView = view;

        ibtn_yes.setVisibility(needsPlace ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (Utils.equals(v, ibtn_yes)) {
            if (ibtn_yes.getVisibility() == View.VISIBLE && selectedPostion != -1) {
                Intent intent = new Intent();
                intent.putExtra("place", placeList.get(selectedPostion));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void queryCollection () {
        if (currentUser != null) {
            BmobQuery<Person> query = new BmobQuery<>();
            //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.getObject(context, currentUser.getObjectId(), new GetListener<Person>() {
                @Override
                public void onSuccess(Person person) {
                    userCollection = person.getCollection();
                    setAdapter(false);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    private void setAdapter (boolean flag) {
        if (readyToSetAdpater || flag) {
            placeAdapter = new PlaceAdapter(context, placeList, userCollection, userLocation);
            lv_places.setAdapter(placeAdapter);
            my_loading.hide();
        }
        readyToSetAdpater = true;
    }
}
