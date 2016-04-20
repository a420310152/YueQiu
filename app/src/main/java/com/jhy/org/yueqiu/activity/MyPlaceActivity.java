package com.jhy.org.yueqiu.activity;

import android.app.Activity;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.PlaceAdapter;
import com.jhy.org.yueqiu.test.h.BaiduMapLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class MyPlaceActivity extends Activity implements View.OnClickListener {
    private Context context = this;

    private Button btn_back;
    private ListView lv_places;

    private List<PoiInfo> placeList;
    private PlaceAdapter placeAdapter;

    private BaiduMapLayout baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_place);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(context, placeList);
        lv_places = (ListView) findViewById(R.id.lv_places);

        baiduMap = (BaiduMapLayout) findViewById(R.id.baiduMap);
    }

    @Override
    public void onClick(View v) {

    }
}
