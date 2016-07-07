package com.jhy.org.yueqiu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.OnReceiveUserLocationListener;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.view.LoadingImageView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchFriendActivity extends Activity implements AdapterView.OnItemClickListener, OnReceiveUserLocationListener {
    private static final int MSG_FILL_FRIENDS = 0x32;
    private Context context = this;
    private Intent myProfileIntent;

    private LoadingImageView my_loading;
    private ListView lv_friends;
    private List<Person> friendList = null;
    private FriendAdapter friendAdapter = null;

    private static Logx logx = new Logx(SearchFriendActivity.class);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("ilog:", "handleMessage, 准备填充ListView");
            lv_friends.setAdapter(friendAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        my_loading.show();
    }

    private void initView () {
        my_loading = (LoadingImageView) findViewById(R.id.my_loading);

        this.lv_friends = (ListView) findViewById(R.id.lv_friends);
        lv_friends.setOnItemClickListener(this);

        this.myProfileIntent = new Intent(context, OpponentActivity.class);

        App.registerReceiveUserLocation(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = friendList.get(position);
        myProfileIntent.putExtra("person", person);
        myProfileIntent.putExtra("action", "request");
        startActivity(myProfileIntent);
    }

    @Override
    public void onReceiveUserLocation(BDLocation userLocation) {
        BmobGeoPoint point = new BmobGeoPoint(userLocation.getLongitude(), userLocation.getLatitude());
        new BmobQuery<Person>()
            .addWhereWithinMiles("location", point, 10000)
            .findObjects(context, new FindListener<Person>() {
                @Override
                public void onSuccess(List<Person> list) {
                    friendList = list;
                    friendAdapter = new FriendAdapter(context, list);
                    handler.sendEmptyMessage(MSG_FILL_FRIENDS);
                    my_loading.hide();
                }

                @Override
                public void onError(int i, String s) {
                    logx.e("查找附近朋友 失败：" + s);
                    showToast("不好意思, 查找失败!");
                }
            });
    }

    private void showToast (CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
