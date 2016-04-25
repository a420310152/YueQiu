package com.jhy.org.yueqiu.test.h;

import android.app.Activity;
import com.jhy.org.yueqiu.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {
    private BaiduMapLayout baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_h);

//        baiduMap = (BaiduMapLayout) findViewById(R.id.baiduMap);

//        play_sex.setYearPickerVisible(false);
//        play_sex.setSecondPickerVisible(false);

//        play_sex = (PickerLayout) findViewById(R.id.play_sex);
//        play_sex.setTitle("请选择性别");
//        String[] values = new String[] {"男", "女"};
//        play_sex.setValues(values);
    }

    public void testClick (View view) {
//        baiduMap.setVisibility(View.VISIBLE);
        startActivity(new Intent(this, ConversationActivity.class));
    }

}
