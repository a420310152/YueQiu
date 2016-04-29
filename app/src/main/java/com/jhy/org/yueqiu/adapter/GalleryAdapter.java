package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Person> mDatas;
    Context context;


    public GalleryAdapter(Context context, List<Person> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTxt;
        ImageView mImg;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_response_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.iv_head);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.i("getAvatarUrl","mDatas.get(i).getAvatarUrl()======="+mDatas.get(i).getAvatarUrl());
        if (mDatas.get(i).getAvatarUrl() != null) {

            Picasso.with(context)
                    .load(mDatas.get(i).getAvatarUrl())
                    .transform(new RoundTransform())
                    .into(viewHolder.mImg);  //设置用户头像
        }
        viewHolder.mTxt.setText(mDatas.get(i).getUsername());
    }
}
