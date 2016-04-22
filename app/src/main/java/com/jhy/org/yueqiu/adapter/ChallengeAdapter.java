package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.ResponseChallengeActivity;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.view.ChallengeLayout;

import java.util.List;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class ChallengeAdapter extends MyBaseAdapter<Challenge> {
    ChallengeLayout challengeLayout;
    ImageView iv_head;
    Challenge challenge;
    TextView tv_apply;
    public ChallengeAdapter(List<Challenge> list, Context context) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        if (convertView==null){
            convertView = inflater.inflate(R.layout.adapter_challenge,null);
            challengeLayout = (ChallengeLayout) convertView.findViewById(R.id.challengeContent);
            iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_apply = (TextView) convertView.findViewById(R.id.tv_apply);
            tv_apply.setId(1000 + position);
        }
        tv_apply.setOnClickListener(click);
        challenge = list.get(position);
        challengeLayout.setContent(challenge);//调用ChallengeLayout类里的方法设置内容
        return convertView;
    }
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            challenge = list.get(v.getId() - 1000);
            Intent intent = new Intent(context,ResponseChallengeActivity.class);
            intent.putExtra("challenge",challenge);
            context.startActivity(intent);
        }
    };
}
