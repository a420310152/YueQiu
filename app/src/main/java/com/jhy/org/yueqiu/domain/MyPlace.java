package com.jhy.org.yueqiu.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jhy.org.yueqiu.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class MyPlace extends PoiInfo {
    public int distance = 0;
    public boolean collected = false;      // 是否已被收藏

    public MyPlace (PoiInfo info) {
        this.name = info.name;
        this.address = info.address;
        this.uid = info.uid;
        this.location = info.location;
    }

    public MyPlace (PoiDetailResult detailResult) {
        this.name = detailResult.getName();
        this.address = detailResult.getAddress();
        this.uid = detailResult.getUid();
        this.location = detailResult.getLocation();
    }
}
