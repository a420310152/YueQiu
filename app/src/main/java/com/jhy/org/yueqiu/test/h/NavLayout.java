package com.jhy.org.yueqiu.test.h;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class NavLayout extends RelativeLayout implements View.OnClickListener {
    private Button btn_communicate;
    private Button btn_search;
    private Context context;

    public NavLayout(Context context) {
        this(context, null);
    }

    public NavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    private void inflateView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_nav, this);
        btn_communicate = (Button) findViewById(R.id.btn_communicate);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_communicate.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
