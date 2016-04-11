package com.jhy.org.yueqiu;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jhy.org.yueqiu.domain.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "f250b019e456d9655d4467d4959ee79e");
        addDate();
    }

    private void addDate(){
        Person p2 = new Person();
            p2.setName("张三");
            p2.setAddress("湖北武汉");
            p2.save(this, new SaveListener() {
                @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"添加数据成功，返回objectId为：",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MainActivity.this,"创建数据失败：" + s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
