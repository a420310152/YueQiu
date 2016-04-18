package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.test.h.DatetimePickerLayout;
import com.jhy.org.yueqiu.test.h.MyDateUtils;
import com.jhy.org.yueqiu.view.OnValuePickedListener;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class EditChallengeActivity extends Activity implements OnValuePickedListener {
    private EditText et_fromDate;
    private EditText et_toDate;
    private EditText et_place;
    private EditText et_title;
    private Button btn_publish;
    private DatetimePickerLayout my_picker;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_challenge);

        et_fromDate = (EditText) findViewById(R.id.et_fromDate);
        et_toDate = (EditText) findViewById(R.id.et_toDate);
        et_place = (EditText) findViewById(R.id.et_place);
        et_title = (EditText) findViewById(R.id.et_title);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        my_picker = (DatetimePickerLayout) findViewById(R.id.my_picker);

        my_picker.setYearPickerVisible(false);
        my_picker.setSecondPickerVisible(false);
        my_picker.setVisibility(View.INVISIBLE);
        my_picker.setOnValuePickedListener(this);
    }

    // 发布一条挑战记录
    public void publish (View view) {
        String _fromDate = et_fromDate.getText().toString();
        String _toDate = et_toDate.getText().toString();
        String _place = et_place.getText().toString();
        String _title = et_title.getText().toString();

        _fromDate = "2016-04-15 09:39:02";
        _toDate = "2026-04-15 12:45:00";
        _place = "随便地点";

        Challenge challenge = new Challenge();
        challenge.setFromDate(new BmobDate(MyDateUtils.getDate()));
        challenge.setToDate(new BmobDate(MyDateUtils.getDate(_toDate)));
        challenge.setTitle(_title);
        challenge.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i("ilog: saveChallenge", "success");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("ilog: saveChallenge", "failed, " + s);
            }
        });
    }

    public void showDatetimePicker (View view) {
        my_picker.setVisibility(View.VISIBLE);
    }

    @Override
    public void onValuePicked(String value) {

    }
}
