package com.jhy.org.yueqiu.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.utils.Logx;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class SubConversationListActivity extends FragmentActivity {
    private Context context = this;
    private static Logx logx = new Logx(SubConversationListActivity.class);

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_sub_conversation_list);
        logx.e("icon_hello!");
        enterFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void enterFragment () {
        SubConversationListFragment fragment = new SubConversationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_conversationlist, fragment);
        transaction.commit();

        RongIM rong = RongIM.getInstance();
        if (rong != null) {
            rong.startSubConversationList(context, Conversation.ConversationType.SYSTEM);
        }
    }
}
