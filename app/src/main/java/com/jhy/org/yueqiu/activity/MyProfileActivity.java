package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.ImageLoader;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.view.ActionBarLayout;
import com.jhy.org.yueqiu.view.OnValuePickedListener;
import com.jhy.org.yueqiu.view.PickerLayout;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.jhy.org.yueqiu.R.id.actionbar_profile_title;
import static com.jhy.org.yueqiu.R.id.btn_info_send;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyProfileActivity extends Activity implements OnValuePickedListener,OnClickListener{
    private ImageView iv_info_head;
    private ActionBarLayout actionBarLayout;
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
    private int value;
    private RelativeLayout relat_info_sex;
    private RelativeLayout relat_info_age;
    private RelativeLayout relat_info_height;
    private RelativeLayout relat_info_weight;
    private RelativeLayout relat_info_skilled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
    }

    @Override
    protected void onPause () {
        super.onPause();
        RongUtils.saveUserInfo();
    }

    private void init(){
        iv_info_head = (ImageView) findViewById(R.id.iv_info_head);
        et_info_name = (EditText) findViewById(R.id.et_info_name);
        tv_selector_sex = (TextView) findViewById(R.id.tv_selector_sex);
        tv_selector_age = (TextView) findViewById(R.id.tv_selector_age);
        tv_selector_height = (TextView) findViewById(R.id.tv_selector_height);
        tv_selector_weight = (TextView) findViewById(R.id.tv_selector_weight);
        tv_selector_skilled = (TextView) findViewById(R.id.tv_selector_skilled);
        actionBarLayout = (ActionBarLayout) findViewById(R.id.actionbar_profile_title);
        actionBarLayout.setOptionsOnClickListener(click);
        btn_info_send = (Button) findViewById(R.id.btn_info_send);
        relat_info_sex = (RelativeLayout) findViewById(R.id.relat_info_sex);
        relat_info_age = (RelativeLayout) findViewById(R.id.relat_info_age);
        relat_info_height = (RelativeLayout) findViewById(R.id.relat_info_height);
        relat_info_weight = (RelativeLayout) findViewById(R.id.relat_info_weight);
        relat_info_skilled = (RelativeLayout) findViewById(R.id.relat_info_skilled);

        pickerLayout = (PickerLayout) findViewById(R.id.info_picker);
        pickerLayout.setOnValuePickedListener(MyProfileActivity.this);
        pickerLayout.setTitle("TITLE");
        my_profile = BmobUser.getCurrentUser(context, Person.class);
        imageLoader = new ImageLoader(MyProfileActivity.this,iv_info_head);
        saveMyProfile();

        //以下是H修改的部分
        //显示用户头像
        String avatar = my_profile.getAvatarUrl();
        Picasso.with(context)
                .load(avatar)
                .transform(new RoundTransform())
                .into(iv_info_head);
    }
    //对编辑按钮进行监听，点击时显示上传按钮
   OnClickListener click = new OnClickListener(){
        @Override
        public void onClick(View v) {
            btn_info_send.setVisibility(View.VISIBLE);
            et_info_name.setFocusable(true);
            et_info_name.setFocusableInTouchMode(true);
            et_info_name.requestFocus();

            relat_info_sex.setOnClickListener(MyProfileActivity.this);
            relat_info_age.setOnClickListener(MyProfileActivity.this);
            relat_info_height.setOnClickListener(MyProfileActivity.this);
            relat_info_weight.setOnClickListener(MyProfileActivity.this);
            relat_info_skilled.setOnClickListener(MyProfileActivity.this);
            btn_info_send.setOnClickListener(MyProfileActivity.this);
        }
    };

    //登录之后，进入个人资料显示你有的信息
    public void saveMyProfile(){
        my_profile_bmobUser = BmobUser.getCurrentUser(context);
        if (my_profile_bmobUser != null) {
            String username = (String) BmobUser.getObjectByKey(context, "username");
            et_info_name.setText(username);
            Boolean usersex = (Boolean) BmobUser.getObjectByKey(context, "sex");
            tv_selector_sex.setText(usersex ? "男" : "女");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageLoader != null) {
            //下面是H修改的部分
//            imageLoader.setResult(requestCode, data);
            uploadFile(imageLoader.setResult(requestCode, data));
        }
    }
    @Override
    public void onClick(View v) {
        currentView = v;
        switch (v.getId()){
            case R.id.relat_info_sex:
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
                String[] skilled = new String[]{"PG", "C","SG","PF","SF"};
                pickerLayout.setValues(skilled);
                break;

            case R.id.btn_info_send:
                pickerLayout.setVisibility(View.INVISIBLE);
                String my_profile_name = et_info_name.getText().toString();
                if(my_profile_name!=null && my_profile!=null) {
                    my_profile.setUsername(my_profile_name);
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
    public void onValuePicked(PickerLayout pickerLayout, int value) {
        if (my_profile == null) {
            return;
        }
        switch (currentView.getId()){
            case R.id.relat_info_sex:
                String sex = pickerLayout.getValue();
                logx.e("选择 性别：" + sex);
                my_profile.setSex(pickerLayout.getValue().equals("男"));
                tv_selector_sex.setText(pickerLayout.getValue());
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
                my_profile.setPosition(pickerLayout.getValue());
                tv_selector_skilled.setText(pickerLayout.getValue());
                break;
        }

    }

    //下面是H修改的部分
    static private Logx logx = new Logx(MyProfileActivity.class);
    private BmobFile bmobFile;
    private String avatarUrl = "";

    private void uploadFile (File file) {
        if (file == null) {
            return;
        }

        bmobFile = new BmobFile(file);
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                logx.e("上传文件 成功: url = " + bmobFile.getFileUrl(context));

                avatarUrl = bmobFile.getFileUrl(context);
                Person person = new Person();
                person.setAvatarUrl(avatarUrl);
                person.update(context, my_profile.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        logx.e("更新用户头像 成功!");
                        String userId = my_profile.getObjectId();
                        String username = my_profile.getUsername();
                        RongUtils.refreshUserInfo(userId, username, avatarUrl);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        logx.e("更新用户头像 失败!");
                        //更新失败, 应该删除网络上已上传的头像
                        //这里暂时不操作
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("上传文件 失败: url = " + bmobFile.getFileUrl(context));
                logx.e("\t\t\t" + s);
            }
        });

    }





}
