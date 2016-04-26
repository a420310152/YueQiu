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
import cn.bmob.v3.listener.UpdateListener;

public class ApplyAdapter extends MyBaseAdapter<Challenge>{
    private TextView tv_apply_type;
    private ImageView iv_apply_head;
    private TextView tv_apply_text;
    private TextView tv_apply_Name;
    private TextView tv_apply_Time;
    private TextView tv_apply_Place;
    private TextView tv_apply;
    private Challenge challenge;
    private Context context;

    public ApplyAdapter(Context context, List<Challenge> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        if (convertView==null) {
            convertView = inflater.inflate(R.layout.adapter_challenge, null);
            iv_apply_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_apply = (TextView) convertView.findViewById(R.id.tv_apply);

            tv_apply_type = (TextView) convertView.findViewById(R.id.tv_title);
            tv_apply_Name = (TextView) convertView.findViewById(R.id.tv_setName);
            tv_apply_Time = (TextView) convertView.findViewById(R.id.tv_setTime);
            tv_apply_Place = (TextView) convertView.findViewById(R.id.tv_setPlace);
            tv_apply_text = (TextView) convertView.findViewById(R.id.tv_text);
            tv_apply.setVisibility(View.INVISIBLE);
        }
        challenge = list.get(position);
        setInfo(challenge);
        return convertView;
    }
    //一对多关联查询
    public void setInfo(final Challenge challenge){
        Log.i("result", "134234~~~~~~~~~~~~~~132123");
        this.challenge = challenge;
        /*BmobQuery<Challenge> query = new BmobQuery<Challenge>();
        if(context!=null) {
            Person person = BmobUser.getCurrentUser(context, Person.class);
            person.setObjectId("1");
            query.addWhereEqualTo("person", new BmobPointer(person));
            query.include("type,initiator,fromDate,placeName");
            query.findObjects(context, new FindListener<Challenge>() {

                @Override
                public void onSuccess(List<Challenge> list) {
                    list.size();
                    tv_apply_Place.setText(challenge.getPlaceName());
                    //由于在列表challenge中是以String类型存在  所以不用特别查询  直接设置
                    tv_apply_type.setText(challenge.getType());
                    //截取只显示日期的字符
                    String data = challenge.getFromDate().getDate();
                    data = data.substring(5, 16);
                    tv_apply_Time.setText(data);
                    tv_apply_text.setText(challenge.getTitle());
                }

                @Override
                public void onError(int code, String msg) {
                    Log.i("result", "!!!!!!!!!!!!!!!!!!!!++++++++++++++!!!!!!!!!!!!!!");
                }
            });
        }*/

        if(context!=null) {
            BmobQuery<Challenge> query = new BmobQuery<Challenge>();
            //challenge.setObjectId("1");
            query.addWhereRelatedTo("responders", new BmobPointer(challenge));
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
                    tv_apply_text.setText(challenge.getTitle());
                }

                @Override
                public void onError(int code, String msg) {

                }
            });

        }
    }
}
