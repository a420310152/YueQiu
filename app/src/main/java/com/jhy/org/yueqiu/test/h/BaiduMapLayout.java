package com.jhy.org.yueqiu.test.h;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/20 0020.
 */
public class BaiduMapLayout extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private MapView mapView;
    private TextView tv_title;

    private BitmapDescriptor icon_a;
    private BitmapDescriptor icon_b;

    private BaiduMap baiduMap;
    private Marker marker_a = null;

    public BaiduMapLayout(Context context) {
        this(context, null);
    }
    public BaiduMapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_baidu_map, this);

        this.context = context;
        mapView = (MapView) findViewById(R.id.mapView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("");
        tv_title.setOnClickListener(this);

        baiduMap = mapView.getMap();
        icon_a = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_a);
        icon_b = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_b);

        setVisibility(INVISIBLE);
    }

    public void setTitle (String title) {
        tv_title.setText(title);
    }

    public void setPosition (LatLng position) {
        if (marker_a != null) {
            marker_a.remove();
        }
        OverlayOptions options = new MarkerOptions()
                .position(position)
                .icon(icon_a);
        marker_a = (Marker) baiduMap.addOverlay(options);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title) {
            setVisibility(INVISIBLE);
        }
    }
}
