package com.jhy.org.yueqiu.activity;

import android.app.Activity;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.GalleryAdapter;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Post;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ResponseChallengeActivity extends Activity {
    Challenge challenge;
    TextView tv_name;
    TextView tv_type;
    TextView tv_time;
    TextView tv_place;
    TextView tv_OK;
    TextView tv_cancle;
    CheckBox cb_helper;
    Context context = this;
    int hourOfDay;
    int minute;
    Calendar calendar;
    AlarmManager manager;
    PendingIntent operation;
    Person person;
    private RecyclerView mRecyclerView;//响应者RecyclerView
    private GalleryAdapter mAdapter;

    public ResponseChallengeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_challenge);
        build();
        Intent intent = getIntent();
        challenge = (Challenge) intent.getSerializableExtra("challenge");
        setContent();
        buildResponse();
    }

    private void build() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_OK = (TextView) findViewById(R.id.tv_OK);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        cb_helper = (CheckBox) findViewById(R.id.cb_helper);
        cb_helper.setOnCheckedChangeListener(click);
        tv_OK.setOnClickListener(clickOk);
        tv_cancle.setOnClickListener(clickCancle);

    }

    //设置点击确认报名监听
    View.OnClickListener clickOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            person = BmobUser.getCurrentUser(context, Person.class);//得到当前用户的对象
            BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
            bmobQuery.addWhereRelatedTo("responders", new BmobPointer(challenge));
            bmobQuery.findObjects(context, new FindListener<Person>() {
                @Override
                public void onSuccess(List<Person> list) {
                    int i = 0;
                    for (Person p : list) {
                        if (p.getObjectId().equals(person.getObjectId())) {
                            Toast.makeText(context, "您已经报名，不要重复报名哦！", Toast.LENGTH_SHORT).show();
                            i = 1;
                        }
                    }
                    if (i != 1&&person.getObjectId()!=challenge.getInitiator().getObjectId()) {
                        BmobRelation responders = new BmobRelation();
                        responders.add(person);//将用户对象添加到多对多关联
                        challenge.setResponders(responders);
                        challenge.update(context, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ResponseChallengeActivity.this, "报名成功！请您准时赴约哦！", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                            }
                        });
                    }


                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }
    };
    //设置取消报名监听
    View.OnClickListener clickCancle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //设置闹钟监听
    CompoundButton.OnCheckedChangeListener click = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                myDialog();
            } else if (isChecked == false && manager != null) {
                manager.cancel(operation);
            }
        }
    };

    //弹出的闹钟
    private void myDialog() {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(context, listener, hourOfDay, minute, true);
        dialog.show();
    }

    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            Intent intent = new Intent(context, AlarmActivity.class);
            operation = PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.set(AlarmManager.RTC, calendar.getTimeInMillis(), operation);
        }
    };

    //左上角 返回键
    public void loginMenu(View v) {
        finish();
    }

    //设置该页面的信息
    private void setContent() {
        Log.i("re", "challenge.getInitiator().getUsername()====" + challenge.getInitiator().getUsername());
        tv_name.setText(challenge.getInitiator().getUsername());
        tv_type.setText(challenge.getType());
        tv_time.setText(challenge.getFromDate().getDate() + "");
        tv_place.setText(challenge.getPlaceName());
    }
    //建立响应者显示列表
    private void buildResponse(){
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //查询Bmob里  响应者
        BmobQuery<Person> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereRelatedTo("responders",new BmobPointer(challenge));
        bmobQuery.findObjects(this, new FindListener<Person>() {
            @Override
            public void onSuccess(List<Person> list) {
                Log.i("responders","responders"+list.size());
                //设置适配器
                mAdapter = new GalleryAdapter(ResponseChallengeActivity.this, list);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
