package com.jhy.org.yueqiu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;
import com.jhy.org.yueqiu.domain.Person;

import java.util.ArrayList;
import java.util.List;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchFriendActivity extends Activity implements AdapterView.OnItemClickListener, BDLocationListener {
    private ListView lv_friends;

    private Context context = this;
    private List<Person> friends;
    private FriendAdapter friendAdapter;
    private Intent myProfileIntent;

    private LocationClient locationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        this.lv_friends = (ListView) findViewById(R.id.lv_friends);
        this.myProfileIntent = new Intent(context, MyProfileActivity.class);
        this.friends = new ArrayList<Person>();
        this.friendAdapter = new FriendAdapter();
        this.locationClient = new LocationClient(getApplicationContext());

        lv_friends.setOnItemClickListener(this);
        locationClient.registerLocationListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = friends.get(position);
        myProfileIntent.putExtra("who", person);
        startActivity(myProfileIntent);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }
}
