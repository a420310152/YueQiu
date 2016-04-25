package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.PickerLayout;
import com.jhy.org.yueqiu.utils.ImageLoader;
import com.jhy.org.yueqiu.view.OnValuePickedListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyProfileActivity extends Activity implements OnValuePickedListener{
    private ImageView iv_info_head;
    private Button btn_info_edit;
    private EditText et_info_name;
    private TextView tv_selector_sex;
    private TextView tv_selector_age;
    private TextView tv_selector_height;
    private TextView tv_selector_weight;
    private TextView tv_selector_skilled;
    private Button btn_info_send;
    private PickerLayout pickerLayout;
    private View currentView;
    private Person my_profile;
    private Context context = this;
    private BmobUser my_profile_bmobUser;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
    }
    private void init(){
        iv_info_head = (ImageView) findViewById(R.id.iv_info_head);
        btn_info_edit = (Button) findViewById(R.id.btn_info_edit);
        et_info_name = (EditText) findViewById(R.id.et_info_name);
        tv_selector_sex = (TextView) findViewById(R.id.tv_selector_sex);
        tv_selector_age = (TextView) findViewById(R.id.tv_selector_age);
        tv_selector_height = (TextView) findViewById(R.id.tv_selector_height);
        tv_selector_weight = (TextView) findViewById(R.id.tv_selector_weight);
        tv_selector_skilled = (TextView) findViewById(R.id.tv_selector_skilled);
        btn_info_send = (Button) findViewById(R.id.btn_info_send);
        pickerLayout = (PickerLayout) findViewById(R.id.info_picker);
        pickerLayout.setOnValuePickedListener(MyProfileActivity.this);
        btn_info_edit.setOnClickListener(click);
        pickerLayout.setTitle("TITLE");
        my_profile = BmobUser.getCurrentUser(context, Person.class);
        imageLoader = new ImageLoader(MyProfileActivity.this,iv_info_head);
        saveMyProfile();
    }
    //对编辑按钮进行监听，点击时显示上传按钮
    OnClickListener click = new OnClickListener(){
        @Override
        public void onClick(View v) {
            btn_info_send.setVisibility(View.VISIBLE);
        }
    };

    //登录之后，进入个人资料显示你有的信息
    public void saveMyProfile(){
        my_profile_bmobUser = BmobUser.getCurrentUser(context);
        if (my_profile_bmobUser != null) {
            String username = (String) BmobUser.getObjectByKey(context, "username");
            et_info_name.setText(username);
            Boolean usersex = (Boolean) BmobUser.getObjectByKey(context, "sex");
            if(usersex==null){
                tv_selector_sex.setText("");
            }else if(usersex=true){
                tv_selector_sex.setText("男");
            }else if(usersex=false){
                tv_selector_sex.setText("女");
            }
            Integer userage = (Integer) BmobUser.getObjectByKey(context, "age");
            if(userage==null){
                tv_selector_age.setText("");
            }else{
                tv_selector_age.setText(userage+" 岁");
            }
            Integer userheight = (Integer) BmobUser.getObjectByKey(context, "height");
            if(userheight==null){
                tv_selector_height.setText("");
            }else{
                tv_selector_height.setText(userheight+" cm");
            }
            Integer userweight = (Integer) BmobUser.getObjectByKey(context, "weight");
            if(userweight==null){
                tv_selector_weight.setText("");
            }else{
                tv_selector_weight.setText(userweight+" kg");
            }
            String userposition = (String) BmobUser.getObjectByKey(context, "position");
            tv_selector_skilled.setText(userposition);
        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }

    public void myProfileBackClick(View v){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageLoader != null) {
            imageLoader.setResult(requestCode, data);
        }
    }

    public void setInfoClick(View v){
        currentView = v;
        switch (v.getId()){
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
                pickerLayout.setVisibility(View.INVISIBLE);
                String my_profile_name = et_info_name.getText().toString();
                String path = Environment.getExternalStorageDirectory()+"avatar.jpg";
                BmobFile file=new BmobFile(new File(path));
                if(my_profile_name!=null && my_profile!=null) {
                    my_profile.setUsername(my_profile_name);
                    my_profile.setAvatar(file);
                    my_profile.update(MyProfileActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MyProfileActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(MyProfileActivity.this, "上传失败" + s, Toast.LENGTH_SHORT).show();
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
                tv_selector_age.setText(value+" 岁");
                break;
            case R.id.relat_info_height:
                my_profile.setHeight(value);
                tv_selector_height.setText(value+" cm");
                break;
            case R.id.relat_info_weight:
                my_profile.setWeight(value);
                tv_selector_weight.setText(value+ " kg");
                break;
            case R.id.relat_info_skilled:
                my_profile.setPosition(picker.getValue());
                tv_selector_skilled.setText(picker.getValue());
                break;
        }

    }
}
