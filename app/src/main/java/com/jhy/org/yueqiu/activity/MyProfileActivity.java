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
public class MyProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }
    public void myProfileBackClick(View v){
        finish();
    }
    public void setInfoClick(View v){
        switch (v.getId()){
            case R.id.iv_info_head:

                break;
            case R.id.linear_info_sex:

                break;
            case R.id.relat_info_age:

                break;
            case R.id.relat_info_height:

                break;
            case R.id.relat_info_weight:

                break;
            case R.id.relat_info_skilled:

                break;
            case R.id.btn_info_send:

                break;
        }
    }


}
