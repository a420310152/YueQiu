package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.ResponseChallengeActivity;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.ChallengeLayout;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

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
    Context context;
    Person person;

    public ChallengeAdapter(List<Challenge> list, Context context) {
        super(context, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_challenge, null);
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

    //点击约战报名按钮
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //跳转至报名页面
            person = BmobUser.getCurrentUser(context, Person.class);//得到当前用户的对象
            if (person!=null){
                challenge = list.get(v.getId()-1000);
                Intent intent = new Intent(context, ResponseChallengeActivity.class);
                intent.putExtra("challenge", challenge);
                context.startActivity(intent);
            }else{
                Toast.makeText(context,"请登录账号",Toast.LENGTH_SHORT).show();
            }
        }
    };
}