package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.App;

public class SplashActivity extends Activity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.initialize();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
