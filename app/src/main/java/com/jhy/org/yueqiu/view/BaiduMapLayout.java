package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.OnReceiveUserLocationListener;

/**
 * Created by Administrator on 2016/4/20 0020.
 */
public class BaiduMapLayout extends RelativeLayout implements View.OnClickListener, OnReceiveUserLocationListener {
    private Context context;
    private MapView mapView;
    private TextView tv_title;

    private BitmapDescriptor icon_a;
    private BitmapDescriptor icon_b;
    private BitmapDescriptor icon_location;

    private BaiduMap baiduMap;

    private Marker marker_a = null;

    public BaiduMapLayout(Context context) {
        this(context, null);
    }
    public BaiduMapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_baidu_map, this);

        this.context = context;

        initView();
        App.registerReceiveUserLocation(this);

        setVisibility(INVISIBLE);
    }

    private void initView () {
        mapView = (MapView) findViewById(R.id.mapView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("");
        tv_title.setOnClickListener(this);

        icon_a = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_a);
        icon_b = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_b);
        icon_location = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_location);

        baiduMap = mapView.getMap();
        
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, icon_location);
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMyLocationConfigeration(config);

        MapStatus status = new MapStatus.Builder().zoom(15).build();
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(status));
    }

    public void setTitle (String title) {
        tv_title.setText(title);
    }

    public void setMakerAPos (LatLng pos) {
        if (marker_a != null) {
            marker_a.remove();
        }
        OverlayOptions options = new MarkerOptions()
                .position(pos)
                .icon(icon_a);
        marker_a = (Marker) baiduMap.addOverlay(options);
    }

    public void setPosition (LatLng position) {
        setMakerAPos(position);
    }

    public void setLocation (BDLocation location) {
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        baiduMap.setMyLocationData(data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title) {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        setLocation(userLocation);
    }
}
