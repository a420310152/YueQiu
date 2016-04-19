package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.test.h.DatetimePickerLayout;
import com.jhy.org.yueqiu.test.h.OnPickDatetimeListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class SoloChallengeActivity extends Activity implements OnPickDatetimeListener, View.OnFocusChangeListener  {
    private static final int REQUEST_CODE_FOR_PLACE = 520;

    private Context context = this;

    private EditText currentView;
    private EditText et_fromDate;
    private EditText et_toDate;
    private EditText et_place;
    private EditText et_title;
    private Button btn_publish;
    private DatetimePickerLayout my_picker;

    private Challenge challenge;
    private Place place;
    private Intent searchPlaceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_challenge);

        et_fromDate = (EditText) findViewById(R.id.et_fromDate);
        et_toDate = (EditText) findViewById(R.id.et_toDate);
        et_place = (EditText) findViewById(R.id.et_place);
        et_title = (EditText) findViewById(R.id.et_title);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        my_picker = (DatetimePickerLayout) findViewById(R.id.my_picker);
        challenge = new Challenge();
        searchPlaceIntent = new Intent(context, SearchPlaceActivity.class);

        my_picker.setYearPickerVisible(false);
        my_picker.setSecondPickerVisible(false);
        my_picker.setVisibility(View.INVISIBLE);
        my_picker.setOnPickDatetimeListener(this);

        et_fromDate.setInputType(InputType.TYPE_NULL);
        et_fromDate.setOnFocusChangeListener(this);

        et_toDate.setInputType(InputType.TYPE_NULL);
        et_toDate.setOnFocusChangeListener(this);

        et_place.setInputType(InputType.TYPE_NULL);
        et_place.setOnFocusChangeListener(this);

        et_title.setOnFocusChangeListener(this);
    }

    // 发布一条挑战记录
    public void publish (View view) {
        String _place = et_place.getText().toString();
        String _title = et_title.getText().toString();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        boolean visible = false;
        currentView = null;

        switch (v.getId()) {
            case R.id.et_fromDate:
                visible = true;
                currentView = et_fromDate;
                break;
            case R.id.et_toDate:
                visible = true;
                currentView = et_toDate;
                break;
            case R.id.et_title:
                visible = false;
                break;
            case R.id.et_place:
                visible = false;
                searchPlaceIntent.putExtra("needsPlace", true);
                startActivityForResult(searchPlaceIntent, REQUEST_CODE_FOR_PLACE);
                break;
            default:
                visible = false;
                break;
        }
        my_picker.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_PLACE) {
                place = (Place) data.getSerializableExtra("place");
            }
        }
    }

    @Override
    public void onPickDatetime(DatetimePickerLayout picker, String value) {
        if (currentView != null) {
            currentView.setText(value);

            if (currentView == et_fromDate) {
                challenge.setFromDate(new BmobDate(picker.getDatetime()));
            } else if (currentView == et_toDate) {
                challenge.setToDate(new BmobDate(picker.getDatetime()));
            }
        }
    }
}
