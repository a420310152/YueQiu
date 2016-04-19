package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.test.h.PickerLayout;
import com.jhy.org.yueqiu.view.OnValuePickedListener;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyProfileActivity extends Activity implements OnValuePickedListener{
    ImageView iv_info_head;
    EditText et_info_name;
    TextView tv_selector_sex;
    TextView tv_selector_age;
    TextView tv_selector_height;
    TextView tv_selector_weight;
    TextView tv_selector_skilled;
    PickerLayout pickerLayout;
    View currentView;
    Person my_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
    }
    private void init(){
        iv_info_head = (ImageView) findViewById(R.id.iv_info_head);
        et_info_name = (EditText) findViewById(R.id.et_info_name);
        tv_selector_sex = (TextView) findViewById(R.id.tv_selector_sex);
        tv_selector_age = (TextView) findViewById(R.id.tv_selector_age);
        tv_selector_height = (TextView) findViewById(R.id.tv_selector_height);
        tv_selector_weight = (TextView) findViewById(R.id.tv_selector_weight);
        tv_selector_skilled = (TextView) findViewById(R.id.tv_selector_skilled);
        pickerLayout = (PickerLayout) findViewById(R.id.info_picker);
        pickerLayout.setOnValuePickedListener(MyProfileActivity.this);
        pickerLayout.setTitle("TITLE");

        my_profile = BmobUser.getCurrentUser(this, Person.class);
        if (my_profile == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
    public void myProfileBackClick(View v){
        finish();
    }
    public void setInfoClick(View v){
        currentView = v;
        switch (v.getId()){
            case R.id.iv_info_head:

                break;
            case R.id.linear_info_sex:
                pickerLayout.setVisibility(View.VISIBLE);
                String[] values = new String[]{"男", "女"};
                pickerLayout.setValues(values);
                break;
            case R.id.relat_info_age:
                pickerLayout.setVisibility(View.VISIBLE);
                pickerLayout.setValues(8, 40,"岁");
                break;
            case R.id.relat_info_height:
                pickerLayout.setVisibility(View.VISIBLE);
                pickerLayout.setValues(160, 190,"cm");
                break;
            case R.id.relat_info_weight:
                pickerLayout.setVisibility(View.VISIBLE);
                pickerLayout.setValues(50, 80,"kg");
                break;
            case R.id.relat_info_skilled:
                pickerLayout.setVisibility(View.VISIBLE);
                String[] position = new String[]{"PG", "C","SG","PF","SF"};
                pickerLayout.setValues(position);
                break;
            case R.id.btn_info_send:
                Log.i("result", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                pickerLayout.setVisibility(View.INVISIBLE);
                String my_profile_name = et_info_name.getText().toString();
                if(my_profile_name!=null && my_profile!=null) {
                    my_profile.setUsername(my_profile_name);
                    my_profile.update(MyProfileActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MyProfileActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(MyProfileActivity.this, "更新失败" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onValuePicked(PickerLayout picker, int value) {
        if (my_profile == null) {
            return;
        }
        switch (currentView.getId()){
            case R.id.linear_info_sex:
                my_profile.setSex(picker.getValue() == "男");
                tv_selector_sex.setText(picker.getValue());
                break;
            case R.id.relat_info_age:
                my_profile.setAge(value);
                tv_selector_age.setText("" + value);
                break;
            case R.id.relat_info_height:
                my_profile.setHeight(value);
                tv_selector_height.setText("" + value);
                break;
            case R.id.relat_info_weight:
                my_profile.setWeight(value);
                tv_selector_weight.setText("" + value);
                break;
            case R.id.relat_info_skilled:
                my_profile.setPosition(picker.getValue());
                tv_selector_skilled.setText(picker.getValue());
                break;
        }

    }
}
