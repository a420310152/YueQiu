package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/23.
 */
public class SetttingToastActivity extends Activity {
    private boolean flag = false;
    private TextView tv_system_new_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_toast);
        tv_system_new_message = (TextView) findViewById(R.id.tv_system_new_message);
        tv_system_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_community_commend_red);
                    drawable.setBounds(100, 100, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_system_new_message.setCompoundDrawables(drawable, null, null, null);
                    flag = true;
                } else if (flag == true) {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_community_commend);
                    drawable.setBounds(100, 100, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_system_new_message.setCompoundDrawables(drawable, null, null, null);
                    flag = false;
                }
            }
        });
    }
}
