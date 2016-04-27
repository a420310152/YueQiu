package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>  {
    private LayoutInflater mInflater;
    private List<Person> mDatas;


    public GalleryAdapter(Context context, List<Person> datats){
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        TextView mTxt;
    }
    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view =  mInflater.inflate(R.layout.activity_response_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.tv_name);
        return viewHolder;
    }
    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        //viewHolder.mTxt.setImageResource(mDatas.get(i).getAvatar());  获取头像
        viewHolder.mTxt.setText(mDatas.get(i).getUsername());
    }
}
