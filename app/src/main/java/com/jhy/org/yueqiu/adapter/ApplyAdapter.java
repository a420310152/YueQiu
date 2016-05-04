package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.OpponentActivity;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.view.ChallengeLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class ApplyAdapter extends MyBaseAdapter<Challenge>{
    private TextView tv_type;
    ChallengeLayout challengeLayout;
    private ImageView iv_head;
    private ImageView tv_apply;
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
            tv_apply = (ImageView) convertView.findViewById(R.id.tv_apply);
            iv_head.setTag(position);
        }
        iv_head.setOnClickListener(headClick);
        challenge = list.get(position);
        challengeLayout.setContent(challenge,true);
        /*tv_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BmobQuery<Person> query = new BmobQuery<Person>();
                query.addWhereRelatedTo("responders", new BmobPointer(challenge));
                query.findObjects(context, new FindListener<Person>() {

                    @Override
                    public void onSuccess(List<Person> list) {
                        Log.e("responders", "报名列表响应者的个数" + list);
                        for (Person responders : list) {
                            if (responders.getObjectId().equals(person.getObjectId())) {
                                BmobRelation relation = new BmobRelation();
                                relation.add(challenge.getResponders());
                                relation.remove(person);
                                Log.e("responders", "队员个数" + challenge.getResponders());
                                Log.e("responders", "队员个数" + relation);
                                list.remove(challenge);
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });*/
        //开始设置每一个挑战的发起人头像  此时需要每次都向服务器获取
        if (challenge.getInitiator().getAvatarUrl() != null) {
            Picasso.with(context)
                    .load(challenge.getInitiator().getAvatarUrl())
                    .transform(new RoundTransform())
                    .into(iv_head);
        }
        return convertView;
    }
    //点击头像弹出个人资料
    View.OnClickListener headClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            challenge = list.get((int) v.getTag());
            String type = challenge.getType();
            Intent intent = new Intent(context, OpponentActivity.class);
            intent.putExtra("challenge", challenge);
            Log.i("rea", "challenge===========" + challenge.getInitiator());
            context.startActivity(intent);
        }
    };
}

