package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.LoginActivity;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.MyPlace;

import java.util.Arrays;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class PlaceLayout extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private MyPlace place;
    private Person currentUser;

    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_distance;
    private TextView tv_usedCount;
    private ImageButton ibtn_likes;

    public PlaceLayout(Context context) {
        this(context, null);
    }

    public PlaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_place, this);

        this.context = context;
        currentUser = BmobUser.getCurrentUser(context, Person.class);

        this.iv_image = (ImageView) findViewById(R.id.iv_image);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_address = (TextView) findViewById(R.id.tv_address);
        this.tv_distance = (TextView) findViewById(R.id.tv_distance);
        this.tv_usedCount = (TextView) findViewById(R.id.tv_usedCount);

        this.ibtn_likes = (ImageButton) findViewById(R.id.ibtn_likes);
        ibtn_likes.setOnClickListener(this);
    }

    public void setPlace (MyPlace place) {
        this.place = place;
        tv_name.setText(place.name);
        tv_address.setText(place.address);
        tv_distance.setText("距离: " + place.distance + "m");
        if (place.collected) {
            ibtn_likes.setBackgroundResource(R.drawable.icon_heart_like_1);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibtn_likes) {
            Log.i("ilog", "点击了红心");
            if (currentUser == null) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else if (place.collected) {
                Person person = new Person();
                person.removeAll("collection", Arrays.asList(place.uid));
                person.update(context, currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("ilog", "删除收藏地点成功");
                        place.collected = false;
                        ibtn_likes.setBackgroundResource(R.drawable.icon_heart_like_0);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("ilog", "删除收藏地点失败");
                    }
                });
            } else {
                Person person = new Person();
                person.addUnique("collection", place.uid);
                person.update(context, currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("ilog", "添加收藏地点成功");
                        place.collected = false;
                        ibtn_likes.setBackgroundResource(R.drawable.icon_heart_like_1);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("ilog", "添加收藏地点失败");
                    }
                });
            }
        }
    }
}
