package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.ContactActivity;
import com.jhy.org.yueqiu.activity.SearchActivity;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class NavLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv_contact;
    private TextView tv_search;
    private Context context;
    private Intent communicateIntent;
    private Intent searchIntent;

    public NavLayout(Context context) {
        this(context, null);
    }

    public NavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_nav, this);

        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_search = (TextView) findViewById(R.id.tv_search);
        this.context = context;
        this.communicateIntent = new Intent(context, ContactActivity.class);
        this.searchIntent = new Intent(context, SearchActivity.class);

        tv_contact.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
