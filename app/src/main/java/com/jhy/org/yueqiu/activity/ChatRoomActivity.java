package com.jhy.org.yueqiu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.utils.Logx;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class ChatRoomActivity extends FragmentActivity {

    private String targetId;
    private String targetIds;
    private Conversation.ConversationType conversationType;
    private static Logx logx = new Logx(ChatRoomActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        getIntentData(getIntent());
        //onSendMessage();
    }

    private void getIntentData (Intent intent) {
        targetId = intent.getData().getQueryParameter("targetId");
        targetIds = intent.getData().getQueryParameter("targetIds");

        String type = intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()); //获得当前会话类型
        conversationType = Conversation.ConversationType.valueOf(type);

        enterFragment(conversationType, targetId);
    }

    private void enterFragment (Conversation.ConversationType type, String targetId) {
        ConversationFragment fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_conversation, fragment);
        transaction.commit();
    }

    private void onSendMessage () {
        RongIM rong = RongIM.getInstance();
        if (rong != null) {
            rong.setSendMessageListener(new RongIM.OnSendMessageListener() {
                @Override
                public Message onSend(Message message) {
                    logx.e("消息发送 成功 sender: " + message.getSenderUserId());
                    logx.e("\t\t\ttarget:" + message.getTargetId());
                    logx.e("\t\t\tcontent:" + message.getContent().toString());
                    return message;
                }

                @Override
                public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                    return false;
                }
            });
        }
    }
}
