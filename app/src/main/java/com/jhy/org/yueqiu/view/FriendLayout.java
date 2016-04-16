package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class FriendLayout extends RelativeLayout {
    ImageView iv_avatar;
    TextView tv_username;
    TextView tv_signature;
    TextView tv_position;
    RatingBar rat_proficiency;

    public FriendLayout(Context context) {
        this(context, null);
    }

    public FriendLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_friend, this);

        this.iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        this.tv_username = (TextView) findViewById(R.id.tv_username);
        this.tv_signature = (TextView) findViewById(R.id.tv_signature);
        this.tv_position = (TextView) findViewById(R.id.tv_position);
        this.rat_proficiency = (RatingBar) findViewById(R.id.rat_proficiency);
    }

    public FriendLayout setPerson (Person person) {
        if (person != null) {
            tv_username.setText(person.getUsername());
            tv_signature.setText(person.getSignature());
            tv_position.setText(person.getPosition());
        }
        return this;
    }

}
