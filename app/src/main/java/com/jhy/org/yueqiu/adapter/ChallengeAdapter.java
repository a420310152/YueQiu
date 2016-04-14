package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.view.ChallengeLayout;

import java.util.List;
/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class ChallengeAdapter extends MyBaseAdapter {
    ChallengeLayout challengeLayout;
    ImageView iv_head;
    public ChallengeAdapter(List<Challenge> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        if (convertView==null){
            convertView = inflater.inflate(R.layout.layout_item_challenge,null);
            challengeLayout = (ChallengeLayout) convertView.findViewById(R.id.challengeContent);
            iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
        }
        Challenge challenge = new Challenge();
        challenge = (Challenge) list.get(position);
        challengeLayout.setContent(challenge);
        return convertView;
    }
}
