package com.jhy.org.yueqiu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.ChallengeLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class ApplyAdapter extends MyBaseAdapter<Challenge>{
    TextView tv_apply_type;
    ImageView iv_apply_head;
    TextView tv_apply_title;
    TextView tv_apply_Name;
    TextView tv_apply_Time;
    TextView tv_apply_Place;
    TextView tv_apply;
    Challenge challenge;
    Context context;
    ChallengeLayout challengeLayout;

    public ApplyAdapter(Context context, List<Challenge> list) {
        super(context, list);
        Log.i("result", "134234~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~132123");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        Log.i("result","134234~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~132123");
        if (convertView==null) {
            convertView = inflater.inflate(R.layout.adapter_challenge, null);
            challengeLayout = (ChallengeLayout) convertView.findViewById(R.id.challengeContent);
            iv_apply_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_apply = (TextView) convertView.findViewById(R.id.tv_apply);
            tv_apply_type = (TextView) convertView.findViewById(R.id.tv_title);
            tv_apply_Name = (TextView) convertView.findViewById(R.id.tv_setName);
            tv_apply_Time = (TextView) convertView.findViewById(R.id.tv_setTime);
            tv_apply_Place = (TextView) convertView.findViewById(R.id.tv_setPlace);
            tv_apply_title = (TextView) convertView.findViewById(R.id.tv_text);
            tv_apply.setVisibility(View.INVISIBLE);
        }
        challenge = list.get(position);
        //调用ChallengeLayout类里的方法设置内容
        setInfo(challenge);
        return convertView;
    }
    public void setInfo(final Challenge challenge){
        Log.i("result","134234~~~~~~~~~~~~~~+++++++++~~~~~~~~~~~~~~~~132123");
        this.challenge = challenge;
        BmobQuery<Challenge> query = new BmobQuery<Challenge>();
        Person person = BmobUser.getCurrentUser(context, Person.class);
        person.setObjectId("1");
        query.addWhereEqualTo("person", new BmobPointer(person));
        query.include("type,initiator,fromDate,placeName");
        query.findObjects(context, new FindListener<Challenge>() {

            @Override
            public void onSuccess(List<Challenge> list) {
                tv_apply_Place.setText(challenge.getPlaceName());
                //由于在列表challenge中是以String类型存在  所以不用特别查询  直接设置
                tv_apply_type.setText(challenge.getType());
                //截取只显示日期的字符
                String data = challenge.getFromDate().getDate();
                data = data.substring(5, 16);
                tv_apply_Time.setText(data);
                tv_apply_title.setText(challenge.getTitle());
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub

            }
        });
    }

}