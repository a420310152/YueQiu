package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.fragment.SidebarFragment;
import com.jhy.org.yueqiu.test.c.TestActivity;
import com.jhy.org.yueqiu.utils.RongUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 *          所有者 C: (曹昌盛)
 **********************************************
 */
public class LoginActivity extends Activity{
    ImageView iv_login_head;
    EditText et_login_name;
    EditText et_login_password;
    Button btn_login;
    TextView tv_register_text;
    CheckBox cb_login_rememberword;
    ImageView iv_login_qq;
    ImageView iv_login_weixin;
    ImageView iv_login_weibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    //初始化控件
    private void init(){
        iv_login_head = (ImageView) findViewById(R.id.iv_login_head);
        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register_text = (TextView) findViewById(R.id.tv_register_text);
        cb_login_rememberword = (CheckBox) findViewById(R.id.cb_login_rememberword);
        iv_login_qq = (ImageView) findViewById(R.id.iv_login_qq);
        iv_login_weibo = (ImageView) findViewById(R.id.iv_login_weibo);
        iv_login_weixin = (ImageView) findViewById(R.id.iv_login_weixin);
        cb_login_rememberword.setOnCheckedChangeListener(checkedChange);

    }
    //对checkBox进行监听
    OnCheckedChangeListener checkedChange = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    //返回箭头的监听
    public void loginBackClick(View v){
        finish();
    }

    //登录按钮的监听
    public void loginClick(View v) {
        String loginUserName = et_login_name.getText().toString();
        String loginUserPassword = et_login_password.getText().toString();
        if(loginUserName.equals("")){
            toast("请填写用户名");
            return;
        }
        if(loginUserPassword.equals("")){
            toast("请填写密码");
            return;
        }
        if(loginUserName!=null && loginUserPassword!=null){
            BmobUser bu_login_user = new BmobUser();
            bu_login_user.setUsername(loginUserName);
            bu_login_user.setEmail(loginUserName);
            bu_login_user.setPassword(loginUserPassword);
            bu_login_user.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    toast("登录成功");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    RongUtils.connect();
                }

                @Override
                public void onFailure(int i, String s) {
                    toast("登录失败" + s);
                }
            });
        }
    }

    //注册按钮的监听
    public void registerClick(View v){
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    //三方登陆
    public void loginOnClick(View v){
        switch (v.getId()){
            case R.id.iv_login_weixin:
                loginByWeixin("");
                break;
            case R.id.iv_login_qq:
                loginByqq("");
                break;
            case R.id.iv_login_weibo:
                loginByWeibo("");
                break;
        }
    }

    //三方登陆的微信登录
    private void loginByWeixin(String s){
        JSONObject qqobj;
        try {
            qqobj = new JSONObject(s);
            String token = qqobj.getString("access_token");
            String expires = String.valueOf(qqobj.getLong("expires_in"));
            String openid = qqobj.getString("openid");
            final BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_WEIXIN,token,expires,openid);
            BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

                @Override
                public void onSuccess(JSONObject userAuth) {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("json", userAuth.toString());
                    intent.putExtra("from", authInfo.getSnsType());
                    startActivity(intent);
                }

                @Override
                public void onFailure(int i, String s) {
                    toast("第三方登录失败");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //三方登陆的qq登录
    private void loginByqq(String s){

    }
    //三方登录微博登录
    private void loginByWeibo(String s){

    }

    //提示信息
    private void toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
