package com.jhy.org.yueqiu.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jhy.org.yueqiu.R;

import cn.bmob.v3.BmobUser;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class SignUpActivity extends Activity {
    EditText et_signup_email;//注册的邮箱
    EditText et_signup_name;//注册的用户名
    EditText et_signup_password;//注册的密码
    EditText getEt_signup_repetPassword;//注册的重复密码
    Button btn_signup_register;//注册按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        registerAccount();
    }
    //初始化控件
    private void init(){
        et_signup_email = (EditText) findViewById(R.id.et_signup_email);
        et_signup_name = (EditText) findViewById(R.id.et_signup_name);
        et_signup_password = (EditText) findViewById(R.id.et_signup_password);
        getEt_signup_repetPassword = (EditText) findViewById(R.id.et_signup_repetpassward);
        btn_signup_register = (Button) findViewById(R.id.btn_signup_register);
    }
    //注册账号
    private void registerAccount(){
        //获取输入框中的email，用户名，密码，重复密码
        String userEmail = et_signup_email.getText().toString();
        String userName = et_signup_name.getText().toString();
        String userPassword = et_signup_password.getText().toString();
        String userRepetPassword = getEt_signup_repetPassword.getText().toString();
        //创建用户对象
        BmobUser user = new BmobUser();
        user.setUsername(userEmail);
    }
    //返回箭头的监听
    public void signupBackClick(View v){
        finish();
    }
    //注册按钮的监听
    public  void signupClick(View v){
        finish();
    }
}
