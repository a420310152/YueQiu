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
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.picasso.Picasso;

import io.rong.imkit.utils.StringUtils;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class FriendLayout extends RelativeLayout {
    private Context context;
    private ImageView iv_avatar;
    private TextView tv_username;
    private TextView tv_signature;
    private TextView tv_position;
    private TextView tv_proficiency;

    public FriendLayout(Context context) {
        this(context, null);
    }

    public FriendLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_friend, this);

        this.context = context;
        this.iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        this.tv_username = (TextView) findViewById(R.id.tv_username);
        this.tv_signature = (TextView) findViewById(R.id.tv_signature);
        this.tv_position = (TextView) findViewById(R.id.tv_position);
        this.tv_proficiency = (TextView) findViewById(R.id.rat_proficiency);
    }

    public FriendLayout setPerson (Person person) {
        if (person != null) {
            String username = person.getUsername();
            String signature = person.getSignature();
            String position = person.getPosition();
            String avatar = person.getAvatarUrl();

            if (signature == null || signature.isEmpty()) {
                signature = "该用户还没有签名";
            }

            if (position == null) {
                position = "";
            }
            position = "擅长位置: " + position;

            if (!Utils.isEmpty(avatar)) {
                Picasso.with(context)
                        .load(avatar)
                        .transform(new RoundTransform())
                        .into(iv_avatar);
            }
            tv_username.setText(username);
            tv_signature.setText(signature);
            tv_position.setText(position);
        }
        return this;
    }

}
