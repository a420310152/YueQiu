package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;

/**
 * Created by Administrator on 2016/4/13.
 */
public class ChallengeLayout extends LinearLayout{
    TextView tv_title;
    TextView tv_setName;
    TextView tv_setTime;
    TextView tv_setPlace;
    TextView tv_text;
    Context context;
    TextView tv_apply;
    public ChallengeLayout(Context context) {
        super(context);
        build(context);
    }

    public ChallengeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(context);
    }

    public ChallengeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build(context);
    }
    private void build(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_challenge,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_setName = (TextView) findViewById(R.id.tv_setName);
        tv_setTime = (TextView) findViewById(R.id.tv_setTime);
        tv_setPlace = (TextView) findViewById(R.id.tv_setPlace);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
    }
    public void setContent(Challenge challenge){
//        tv_title.setText(challenge.getTitle());
//        tv_setName.setText(challenge.getPublisher());
//        tv_setTime.setText(challenge.getTime());
//        tv_setPlace.setText(challenge.getPlace());
//        tv_text.setText(challenge.getText());
    }
    public void clickApply(View v){

    }
}
