package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/18.
 */
public class AlarmActivity extends Activity{
    Button btn_cancel;
    Vibrator vibrator;
    long[] pattern = {1000,2000,3000,4000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(click);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 1);
    }
    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }
}
