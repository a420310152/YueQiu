package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jhy.org.yueqiu.domain.MyPlace;
import com.jhy.org.yueqiu.view.PlaceLayout;

import java.util.List;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class PlaceAdapter extends MyBaseAdapter<MyPlace> {
    private LatLng userLocation = null;
    private List<String> userCollection = null;

    public PlaceAdapter(Context context, List<MyPlace> list, List<String> collection, LatLng userLocation) {
        super(context, list);
        for (MyPlace place : list) {
            place.distance = (int) DistanceUtil.getDistance(userLocation, place.location);
            if (collection != null) {
                for (String i : collection) {
                    if (i.equals(place.uid)) {
                        place.collected = true;
                    }
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceLayout layout = (PlaceLayout)convertView;
        if (layout == null) {
            layout = new PlaceLayout(context);
        }
        layout.setPlace(list.get(position));
        return layout;
    }
}
