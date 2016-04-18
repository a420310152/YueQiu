package com.jhy.org.yueqiu.test.h;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.view.NavLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends Activity {
    private PickerLayout play_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_h_popup);

        play_sex = (PickerLayout) findViewById(R.id.play_sex);
        play_sex.setTitle("请选择性别");
        String[] values = new String[] {"男", "女"};
        play_sex.setValues(values);
    }

    public void testClick (View view) {
        Log.i("ilog;", "testClick");
    }

}
