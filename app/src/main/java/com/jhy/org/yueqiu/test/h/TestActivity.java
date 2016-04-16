package com.jhy.org.yueqiu.test.h;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Place;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends Activity {
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_h);
    }

    private void savePlace () {
        this.place = new Place();
        place.setName("This is place");
        place.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i("result:savePlace", "success");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("result:savePlase", "failure");
            }
        });
    }

}
