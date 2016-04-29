package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;

/**
 * Created by Administrator on 2016/4/28.
 */
public class OpponentSoloActivity extends Activity{
    Challenge challenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_solo);
        Intent intent = getIntent();
        challenge = (Challenge) intent.getSerializableExtra("challenge");
    }
}
