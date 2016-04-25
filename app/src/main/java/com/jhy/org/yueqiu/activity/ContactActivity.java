package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.FriendAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ContactActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private TextView tv_title;
    private ImageButton ibtn_back;
    private TextView tv_options;

    private FriendAdapter contactAdapter;
    private ListView lv_contacts;
    /**
     * ids 收消息人的 id
     */
    List<String> concatList = Arrays.asList("56145", "56146", "56147", "56148");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

/*        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("创建讨论组");

        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);

        tv_options = (TextView) findViewById(R.id.tv_options);
        tv_options.setOnClickListener(this);

        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        contactAdapter = new ContactAdapter(ContactsActivity.this, concatList);
        lv_contacts.setAdapter(contactAdapter);
        lv_contacts.setOnItemClickListener(this);*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
        if (RongIM.getInstance() != null)
            RongIM.getInstance().startPrivateChat(ContactsActivity.this, concatList.get(position), "title");*/
    }

    @Override
    public void onClick(View v) {
 /*       switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_options:
                if (RongIM.getInstance() != null)
                *//**
         *创建讨论组时，mLists为要添加的讨论组成员，创建者一定不能在 mLists 中
         *//*
                    RongIM.getInstance().getRongIMClient().createDiscussion("Hello Discussion", concatList, new RongIMClient.CreateDiscussionCallback() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                break;
            default:
                break;
        }*/
    }
}
