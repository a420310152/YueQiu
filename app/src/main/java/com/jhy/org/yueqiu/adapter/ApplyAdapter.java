package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.ChallengeLayout;
import java.util.List;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;

public class ApplyAdapter extends MyBaseAdapter<Challenge>{
    private TextView tv_type;
    ChallengeLayout challengeLayout;
    private ImageView iv_head;
    private TextView tv_apply;
    private Challenge challenge;
    private Person person;

    public ApplyAdapter(Context context, List<Challenge> list) {
        super(context, list);
        person = BmobUser.getCurrentUser(context, Person.class);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        if (convertView==null) {
            convertView = inflater.inflate(R.layout.adapter_challenge, null);
            challengeLayout = (ChallengeLayout) convertView.findViewById(R.id.challengeContent);
            iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_apply = (TextView) convertView.findViewById(R.id.tv_apply);
            tv_apply.setText("删除报名");
        }
        challenge = list.get(position);
        challengeLayout.setContent(challenge);
        tv_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BmobRelation relation = new BmobRelation();
                relation.add(challenge.getResponders());
                person.delete(context, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        list.remove(challenge);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }
        });



        return convertView;
    }
}

