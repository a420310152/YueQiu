package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.model.LatLng;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.view.PlaceLayout;

import java.util.List;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class PlaceAdapter extends MyBaseAdapter<Place> {
    private LatLng userLocation = null;

    public PlaceAdapter(Context context, List<Place> list) {
        super(context, list);
    }

    public void setUserLocation (LatLng userLocation) { this.userLocation = userLocation; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceLayout layout = (PlaceLayout)convertView;
        if (layout == null) {
            layout = new PlaceLayout(context);
        }
        layout.setPlace(list.get(position), userLocation);
        return layout;
    }
}
