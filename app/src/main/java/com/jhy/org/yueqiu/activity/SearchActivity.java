package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SearchActivity extends Activity implements View.OnClickListener {
    private Context context = this;

    private TextView tv_searchFriend;
    private TextView tv_searchPlace;
    private TextView tv_searchSogou;

    private Intent searchFriendIntent;
    private Intent searchPlaceIntent;
    private Intent searchSogouIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tv_searchFriend = (TextView) findViewById(R.id.tv_searchFriend);
        tv_searchFriend.setOnClickListener(this);

        tv_searchPlace = (TextView) findViewById(R.id.tv_searchPlace);
        tv_searchPlace.setOnClickListener(this);

        tv_searchSogou = (TextView) findViewById(R.id.tv_searchSogou);
        tv_searchSogou.setOnClickListener(this);

        searchFriendIntent = new Intent(context, SearchFriendActivity.class);
        searchPlaceIntent = new Intent(context, SearchPlaceActivity.class);
        searchSogouIntent = new Intent(context,CommunityActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_searchFriend:
                startActivity(searchFriendIntent);
                break;
            case R.id.tv_searchPlace:
                startActivity(searchPlaceIntent);
                break;
            case R.id.tv_searchSogou:
               startActivity(searchSogouIntent);
                break;
            default:
                break;
        }
    }
}
