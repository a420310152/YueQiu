package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
