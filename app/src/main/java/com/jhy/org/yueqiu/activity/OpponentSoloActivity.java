package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/4/28.
 */
public class OpponentSoloActivity extends Activity{
    Challenge challenge;
    ImageView iv_head;//头像
    TextView tv_name;//用户名
    TextView tv_position;//位置
    TextView tv_text;//宣言
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_solo);
        Intent intent = getIntent();
        challenge = (Challenge) intent.getSerializableExtra("challenge");
        build();
        setOpponent();
    }
    private void build(){
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_text = (TextView) findViewById(R.id.tv_text);

    }
    private void setOpponent(){
        Picasso.with(this).load(challenge.getInitiator().getAvatarUrl()).into(iv_head);
        tv_name.setText(challenge.getInitiator().getUsername());
        tv_position.setText(challenge.getInitiator().getPosition());
        tv_text.setText(challenge.getTitle());
    }
}
