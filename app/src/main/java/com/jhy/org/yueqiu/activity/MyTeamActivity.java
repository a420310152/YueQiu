package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import android.os.Bundle;
import android.view.View;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyTeamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
    }
    public void myTeamBackClick(View v){
        finish();
    }
}
