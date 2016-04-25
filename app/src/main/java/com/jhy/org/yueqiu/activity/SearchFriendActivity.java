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
import com.jhy.org.yueqiu.config.MyApplication;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.test.h.OnReceiveUserLocationListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchFriendActivity extends Activity implements AdapterView.OnItemClickListener, OnReceiveUserLocationListener, View.OnClickListener {
    private Context context = this;
    private Intent myProfileIntent;

    private ImageButton ibtn_back;

    private ListView lv_friends;
    private List<Person> friendList = null;
    private FriendAdapter friendAdapter = null;

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

        this.lv_friends = (ListView) findViewById(R.id.lv_friends);
        lv_friends.setOnItemClickListener(this);

        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);

        this.myProfileIntent = new Intent(context, MyProfileActivity.class);

        MyApplication.registerReceiveUserLocation(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = friendList.get(position);
        myProfileIntent.putExtra("who", person);
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
                    handler.sendEmptyMessage(1);
                }

                @Override
                public void onError(int i, String s) {
                    Log.i("ilog", "SearchActivity: 查找失败!");
                    showToast("不好意思, 查找失败!");
                }
            });
    }

    private void showToast (CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibtn_back) {
            finish();
        }
    }
}
