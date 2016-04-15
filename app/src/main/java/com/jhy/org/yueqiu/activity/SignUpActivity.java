package com.jhy.org.yueqiu.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jhy.org.yueqiu.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import static android.widget.Toast.LENGTH_LONG;

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

    //获取输入框中的email，用户名，密码，重复密码
    String userEmail,userName,userPassword,userRepetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }
    //初始化控件
    private void init(){
        et_signup_email = (EditText) findViewById(R.id.et_signup_email);
        et_signup_name = (EditText) findViewById(R.id.et_signup_name);
        et_signup_password = (EditText) findViewById(R.id.et_signup_password);
        getEt_signup_repetPassword = (EditText) findViewById(R.id.et_signup_repetpassward);
        btn_signup_register = (Button) findViewById(R.id.btn_signup_register);
    }

    //返回箭头的监听
    public void signupBackClick(View v){
        finish();
    }
    //注册按钮的监听
    public  void signupClick(View v){
        //获取输入框中的email，用户名，密码，重复密码
        userEmail = et_signup_email.getText().toString();
        userName = et_signup_name.getText().toString();
        userPassword = et_signup_password.getText().toString();
        userRepetPassword = getEt_signup_repetPassword.getText().toString();
        signupToastInfo();
        if(userEmail!=null && userName!=null && userPassword!=null && userRepetPassword!=null){
            BmobUser bu_signup_user = new BmobUser();
            bu_signup_user.setEmail(userEmail);
            bu_signup_user.setUsername(userName);
            bu_signup_user.setPassword(userPassword);
            if (userPassword.equals(userRepetPassword)) {
                bu_signup_user.signUp(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SignUpActivity.this, "注册失败" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    //对注册信息的判断
    private void signupToastInfo(){
        if(userEmail.equals("")){
            Toast.makeText(SignUpActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
        }
        if(userName.equals("")){
            Toast.makeText(SignUpActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
        if(userPassword.equals("")){
            Toast.makeText(SignUpActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }
        if(userRepetPassword.equals("")){
            Toast.makeText(SignUpActivity.this, "重复密码不能为空", Toast.LENGTH_SHORT).show();
        }
        if(!userPassword.equals(userRepetPassword)){
            Toast.makeText(SignUpActivity.this, "密码与重复密码不一致", Toast.LENGTH_SHORT).show();
        }
    }
}
