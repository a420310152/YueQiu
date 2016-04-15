package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.List;
/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class HomeGalleryAdapter extends BaseAdapter{
    List<Integer> list;
    Context context;

    public HomeGalleryAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if(convertView==null){
            convertView = new ImageView(context);
        }
        image = (ImageView) convertView;
        //设置控件的大小参数
        Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
        image.setLayoutParams(params);
        image.setBackgroundResource(list.get(position));
        return image;
    }
}
