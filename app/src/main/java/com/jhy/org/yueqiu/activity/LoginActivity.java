package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jhy.org.yueqiu.bmob.BmobUtils;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.fragment.SidebarFragment;
import com.jhy.org.yueqiu.test.c.TestActivity;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.Preferences;
import com.jhy.org.yueqiu.utils.QQUserAuth;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.ThirdUserAuth;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.utils.WeiboUserAuth;
import com.jhy.org.yueqiu.utils.WeixinUserAuth;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

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
    private Context context = this;
    ImageView iv_login_head;
    EditText et_login_name;
    EditText et_login_password;
    Button btn_login;
    TextView tv_register_text;
    CheckBox cb_login_rememberword;
    ImageView iv_login_qq;
    ImageView iv_login_weixin;
    ImageView iv_login_weibo;
    
    private Boolean isRemember = false;
    
    private QQUserAuth qqUserAuth;
    private WeixinUserAuth weixinUserAuth;
    private WeiboUserAuth weiboUserAuth;
    
    private static Logx logx = new Logx(LoginActivity.class);
    private static final int REQUESTCODE_REGIST = 101;
    private Preferences preferences;
    private String name;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_LOGIN && qqUserAuth != null) {
            qqUserAuth.onActivityResultData(requestCode, resultCode, data);
        }
        if (weiboUserAuth != null) {
            weiboUserAuth.onActivityResultData(requestCode, resultCode, data);
        }
    }

    //初始化控件
    private void init(){
        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register_text = (TextView) findViewById(R.id.tv_register_text);
        cb_login_rememberword = (CheckBox) findViewById(R.id.cb_login_rememberword);
        iv_login_qq = (ImageView) findViewById(R.id.iv_login_qq);        iv_login_weixin = (ImageView) findViewById(R.id.iv_login_weixin);
        cb_login_rememberword.setOnCheckedChangeListener(checkedChange);

        iv_login_weibo = (ImageView) findViewById(R.id.iv_login_weibo);
//        iv_login_weibo.setBackgroundResource(R.drawable.icon_login_weibo);
        
        preferences = Preferences.getInstance();
        String name = preferences.get("name", "");
        String pwd = preferences.get("pwd", "");
        isRemember = preferences.get("isRemember", false);
        if (isRemember) {
            et_login_name.setText(name);
            et_login_password.setText(pwd);
            cb_login_rememberword.setChecked(isRemember);
        }
    }
    //对checkBox进行监听
    OnCheckedChangeListener checkedChange = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isRemember = isChecked;
            preferences.set("isRemember", isRemember)
                        .commit();

        }
    };
    //登录按钮的监听
    public void loginClick(View v) {
        String loginUserName = et_login_name.getText().toString();
        String loginUserPassword = et_login_password.getText().toString();
        preferences.set("name", loginUserName)
                .set("pwd", loginUserPassword)
                .commit();
        if(loginUserName.equals("")){
            toast("请填写用户名");
            return;
        }
        if(loginUserPassword.equals("")){
            toast("请填写密码");
            return;
        }
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

                Person currentUser = BmobUtils.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getObjectId();
                    String name = currentUser.getUsername();
                    String avatarUrl = currentUser.getAvatarUrl();

                    if (Utils.isEmpty(avatarUrl)) {
                        logx.e("登录 成功: 用户的 avatarUrl 为空");
                        avatarUrl = Person.URL_DEFAULT_AVATAR;
                    }

                    RongUtils.requestToken(userId, name, avatarUrl);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                toast("登录失败" + s);
            }
        });
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

    private QQUserAuth getQQUserAuth () {
        if (qqUserAuth == null) {
            qqUserAuth = new QQUserAuth(this);
        }
        return qqUserAuth;
    }

    private WeixinUserAuth getWeixinUserAuth () {
        if (weixinUserAuth == null) {
            weixinUserAuth = new WeixinUserAuth(this);
        }
        return weixinUserAuth;
    }

    private WeiboUserAuth getWeiboUserAuth () {
        if (weiboUserAuth == null) {
            weiboUserAuth = new WeiboUserAuth(this);
        }
        return weiboUserAuth;
    }

    //三方登陆的微信登录
    private void loginByWeixin(String s){
        getWeixinUserAuth().login();
    }

    //三方登陆的qq登录
    private void loginByqq (String s){
        getQQUserAuth().login();
    }
    //三方登录微博登录
    private void loginByWeibo (String s){
        getWeiboUserAuth().login();
    }

    //提示信息
    private void toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
