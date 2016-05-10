package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.OpponentActivity;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.squareup.picasso.Picasso;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class TeamLayout extends RelativeLayout {

    ImageView iv_memember_head;
    TextView tv_memember_name_text;
    TextView tv_memember_height_text;
    TextView tv_memember_weight_text;
    TextView tv_memember_position_text;
    Context context;
    Person person;

    public TeamLayout(Context context) {
        this(context, null);
    }

    public TeamLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_team, this);
        this.iv_memember_head = (ImageView) findViewById(R.id.iv_menember_head);
        this.tv_memember_name_text = (TextView) findViewById(R.id.tv_memember_name_text);
        this.tv_memember_height_text = (TextView) findViewById(R.id.tv_memember_height_text);
        this.tv_memember_weight_text = (TextView) findViewById(R.id.tv_memember_weight_text);
        this.tv_memember_position_text = (TextView) findViewById(R.id.tv_memember_position_text);
        iv_memember_head.setOnClickListener(click);
    }

    //点击头像跳转
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OpponentActivity.class);
            intent.putExtra("person",person);
            context.startActivity(intent);
        }
    };

    //设置成员信息
    public void setTeam(Person person) {
        this.person = person;
        Picasso.with(context).load(person.getAvatarUrl()).transform(new RoundTransform()).into(iv_memember_head);
        this.iv_memember_head.setClickable(false);
        this.tv_memember_name_text.setText(person.getUsername());
        this.tv_memember_height_text.setText(person.getHeight() + "cm");
        this.tv_memember_weight_text.setText(person.getWeight() + "kg");
        this.tv_memember_position_text.setText(person.getPosition());
    }

}
