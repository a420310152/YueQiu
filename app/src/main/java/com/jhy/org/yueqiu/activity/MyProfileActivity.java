package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.view.OnValuePickedListener;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class MyProfileActivity extends Activity implements OnValuePickedListener{
    EditText et_info_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        et_info_name = (EditText) findViewById(R.id.et_info_name);
    }
    public void myProfileBackClick(View v){
        finish();
    }
    public void setInfoClick(View v){
        switch (v.getId()){
            case R.id.iv_info_head:

                break;
            case R.id.btn_info_send:
                String my_profile_name = et_info_name.getText().toString();
                Person my_profile = BmobUser.getCurrentUser(this, Person.class);
                my_profile.setUsername(my_profile_name);
                my_profile.update(MyProfileActivity.this,new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MyProfileActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(MyProfileActivity.this,"更新失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }


    @Override
    public void onValuePicked(String value) {
        
    }
}
