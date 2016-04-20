package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ChallengeDetailsActivity extends Activity{
    ListView lv_war;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_challenge);
        build();
    }
    private void build(){
        lv_war = (ListView) findViewById(R.id.lv_war);
    }
}
