package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.view.CommunityReleaseLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CommunityAdapter extends MyBaseAdapter<Post>{

    public CommunityAdapter(Context context, List<Post> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        CommunityReleaseLayout view = (CommunityReleaseLayout) convertView;
        if(view == null){
            view = new CommunityReleaseLayout(context);
        }
        view.setCommunityInfo(list.get(position));
        return view;
    }
}
