package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.view.View.OnClickListener;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.view.AllTeamLayout;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MyAllTeamActivity extends Activity{
    AllTeamLayout allTeamLayout;
    ListView lv_allteam_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allteam);
        init();
    }
    public void allteaminfoClick(View v){
        finish();
    }
    private void init(){
        allTeamLayout = (AllTeamLayout) findViewById(R.id.allteam_mode);
        lv_allteam_info = (ListView) findViewById(R.id.lv_allteam_info);
        allTeamLayout.setOnClickListener(onClick);
        lv_allteam_info.setOnItemClickListener(click);
    }
    //对我创建的球队进行监听
    OnClickListener onClick = new OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent allTeamIntent = new Intent(MyAllTeamActivity.this,MyTeamActivity.class);
            startActivity(allTeamIntent);
        }
    };
    //对我加入的球队列表进行监听
    OnItemClickListener click = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
}
