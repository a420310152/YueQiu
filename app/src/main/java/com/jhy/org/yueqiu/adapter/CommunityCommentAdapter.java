package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jhy.org.yueqiu.domain.Comment;
import com.jhy.org.yueqiu.domain.Post;
import com.jhy.org.yueqiu.view.CommunityReleaseLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CommunityCommentAdapter extends MyBaseAdapter<Comment>{

    public CommunityCommentAdapter(Context context, List<Comment> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommunityReleaseLayout view = (CommunityReleaseLayout) convertView;
        if(view == null){
            view = new CommunityReleaseLayout(context);
        }
        view.setCommentInfo(list.get(position));
        Log.e("getView","传递的comment"+list.get(position));
        return view;
    }
}
