package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.ContactActivity;
import com.jhy.org.yueqiu.activity.SearchActivity;
import com.jhy.org.yueqiu.test.h.PopupSpan;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class NavLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv_contact;
    private TextView tv_search;
    private ImageView iv_basketball;
    private PopupSpan popupSpan;
    private Context context;
    private Intent contactIntent;
    private Intent searchIntent;

    public NavLayout(Context context) {
        this(context, null);
    }

    public NavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_nav, this);

        this.tv_contact = (TextView) findViewById(R.id.tv_contact);
        this.tv_search = (TextView) findViewById(R.id.tv_search);
        this.iv_basketball = (ImageView) findViewById(R.id.iv_basketball);
        this.popupSpan = (PopupSpan) findViewById(R.id.popupSpan);
        this.context = context;
        this.contactIntent = new Intent(context, ContactActivity.class);
        this.searchIntent = new Intent(context, SearchActivity.class);

        tv_contact.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_basketball.setOnClickListener(this);
        popupSpan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contact:
                context.startActivity(contactIntent);
                break;
            case R.id.tv_search:
                context.startActivity(searchIntent);
                break;
            case R.id.iv_basketball:
                if  (popupSpan.getVisibility() == View.GONE) {
                    popupSpan.setVisibility(View.VISIBLE);
                } else {
                    popupSpan.setVisibility(View.GONE);
                }
            default:
                break;
        }
    }
}
