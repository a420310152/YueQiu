package com.jhy.org.yueqiu.activity;

import android.app.Activity;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.test.h.DatetimePickerLayout;
import com.jhy.org.yueqiu.test.h.OnPickDatetimeListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class TeamChallengeActivity extends Activity implements OnPickDatetimeListener, View.OnClickListener {
    private static final int REQUEST_CODE_FOR_PLACE = 520;

    private Context context = this;

    private TextView currentView;
    private TextView tv_fromDate;
    private TextView tv_toDate;
    private TextView tv_place;
    private EditText et_title;
    private Button btn_publish;
    private ImageButton ibtn_finish;
    private DatetimePickerLayout my_picker;

    private Challenge challenge;
    private Person currentUser = null;
    private PoiInfo place = null;

    private Intent searchPlaceIntent;
    private Intent loginIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_challenge);

        searchPlaceIntent = new Intent(context, SearchPlaceActivity.class);
        loginIntent = new Intent(context, LoginActivity.class);

        currentUser = BmobUser.getCurrentUser(context, Person.class);
        if (currentUser == null) {
            startActivity(loginIntent);
            finish();
        }
        challenge = new Challenge();
        challenge.setInitiator(currentUser);
        challenge.setType(Challenge.TYPE_TEAM);

        tv_fromDate = (TextView) findViewById(R.id.tv_fromDate);
        tv_fromDate.setInputType(InputType.TYPE_NULL);
        findViewById(R.id.container_fromDate).setOnClickListener(this);

        tv_toDate = (TextView) findViewById(R.id.tv_toDate);
        tv_toDate.setInputType(InputType.TYPE_NULL);
        findViewById(R.id.container_toDate).setOnClickListener(this);

        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_place.setInputType(InputType.TYPE_NULL);
        tv_place.setOnClickListener(this);
        findViewById(R.id.container_place).setOnClickListener(this);

        et_title = (EditText) findViewById(R.id.et_title);
        findViewById(R.id.container_title).setOnClickListener(this);

        btn_publish = (Button) findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(this);

        ibtn_finish = (ImageButton) findViewById(R.id.ibtn_finish);
        ibtn_finish.setOnClickListener(this);

        my_picker = (DatetimePickerLayout) findViewById(R.id.my_picker);
        my_picker.setYearPickerVisible(false);
        my_picker.setSecondPickerVisible(false);
        my_picker.setVisibility(View.INVISIBLE);
        my_picker.setOnPickDatetimeListener(this);
    }

    // 发布一条挑战记录
    public void publish () {
        String _title = et_title.getText().toString();
        if (_title.equals("") || tv_place.getText().equals("")
                || tv_fromDate.getText().equals("") || tv_toDate.getText().equals("")) {
            showToast("提交错误, 请重新填写");
            return;
        }
        challenge.setTitle(_title);
        challenge.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                showToast("添加成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("添加失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        boolean visible = false;
        currentView = null;

        switch (v.getId()) {
            case R.id.container_fromDate:
                visible = true;
                currentView = tv_fromDate;
                break;
            case R.id.container_toDate:
                visible = true;
                currentView = tv_toDate;
                break;
            case R.id.container_title:
                break;
            case R.id.container_place:
                searchPlaceIntent.putExtra("needsPlace", true);
                startActivityForResult(searchPlaceIntent, REQUEST_CODE_FOR_PLACE);
                break;
            case R.id.btn_publish:
                publish();
                break;
            case R.id.ibtn_finish:
                finish();
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
                place = (PoiInfo) data.getParcelableExtra("place");
                if (place != null) {
                    tv_place.setText(place.name);
                    challenge.setPlaceName(place.name);
                    challenge.setPlaceAddress(place.address);
                    challenge.setPlaceUid(place.uid);
                }
            }
        }
    }

    @Override
    public void onPickDatetime(DatetimePickerLayout picker, String value) {
        if (currentView != null) {
            currentView.setText(value);

            if (currentView == tv_fromDate) {
                challenge.setFromDate(new BmobDate(picker.getDatetime()));
            } else if (currentView == tv_toDate) {
                challenge.setToDate(new BmobDate(picker.getDatetime()));
            }
        }
    }

    private void showToast (String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
