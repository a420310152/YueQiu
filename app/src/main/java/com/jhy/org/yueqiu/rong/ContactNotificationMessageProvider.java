package com.jhy.org.yueqiu.rong;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.utils.Logx;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
@ProviderTag(messageContent = ContactNotificationMessage.class)
public class ContactNotificationMessageProvider extends IContainerItemProvider.MessageProvider<ContactNotificationMessage> {
    private TextView tv_signature;
    private static Logx logx = new Logx(ContactNotificationMessageProvider.class);

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_friend, null);
        tv_signature = (TextView) view.findViewById(R.id.tv_signature);
        return view;
    }

    @Override
    public void bindView(View view, int i, ContactNotificationMessage contactNotificationMessage, UIMessage uiMessage) {
        String sourceUserId = contactNotificationMessage.getSourceUserId();
        String targetUserId = contactNotificationMessage.getTargetUserId();
        String message = contactNotificationMessage.getMessage();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            tv_signature.setText(message);
        }
    }

    @Override
    public Spannable getContentSummary(ContactNotificationMessage contactNotificationMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, ContactNotificationMessage contactNotificationMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, ContactNotificationMessage contactNotificationMessage, UIMessage uiMessage) {

    }
}
