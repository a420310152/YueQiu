package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */
public class TeamLayout extends RelativeLayout{

    ImageView iv_memember_head;
    TextView tv_memember_name_text;
    TextView tv_memember_height_text;
    TextView tv_memember_weight_text;
    TextView tv_memember_position_text;
    public TeamLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_team, this);
        this.iv_memember_head = (ImageView) findViewById(R.id.iv_menember_head);
        this.tv_memember_name_text = (TextView) findViewById(R.id.tv_memember_name_text);
        this.tv_memember_height_text = (TextView) findViewById(R.id.tv_memember_height_text);
        this.tv_memember_weight_text = (TextView) findViewById(R.id.tv_memember_weight_text);
        this.tv_memember_position_text = (TextView) findViewById(R.id.tv_memember_position_text);
    }
    public void setTeam(Person person){
        this.tv_memember_name_text.setText(person.getUsername());
        this.tv_memember_height_text.setText(person.getHeight()+"cm");
        this.tv_memember_weight_text.setText(person.getWeight()+"kg");
        this.tv_memember_position_text.setText(person.getPosition());
    }

}
