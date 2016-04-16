package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Place;
import com.squareup.picasso.Picasso;

import cn.bmob.v3.datatype.BmobGeoPoint;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class PlaceLayout extends RelativeLayout {
    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_distance;
    private TextView tv_usedCount;

    private Context context;

    public PlaceLayout(Context context) {
        this(context, null);
    }

    public PlaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_place, this);

        this.context = context;
        this.iv_image = (ImageView) findViewById(R.id.iv_image);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_address = (TextView) findViewById(R.id.tv_address);
        this.tv_distance = (TextView) findViewById(R.id.tv_distance);
        this.tv_usedCount = (TextView) findViewById(R.id.tv_usedCount);
    }

    public void setPlace (Place where, LatLng userLocation) {
//        Picasso.with(context).load(where.getImage().getUrl()).into(iv_image);
        tv_name.setText(where.getName());
        tv_address.setText(where.getAddress());
//        tv_distance.setText("");
//        tv_usedCount.setText(where.getUsedCount());

        String distance = "*";
        BmobGeoPoint point = where.getLocation();
        if (userLocation != null && point != null) {
            LatLng targetLocation = new LatLng(point.getLatitude(), point.getLongitude());
            distance = "" + (int)DistanceUtil.getDistance(userLocation, targetLocation);
        }
        tv_distance.setText("距离: " + distance + "m");
    }
}
