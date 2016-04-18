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
    TextView tv_memember_name;
    TextView tv_memember_height;
    TextView tv_memember_weight;
    TextView tv_memember_position;
    public TeamLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_team, this);
        this.iv_memember_head = (ImageView) findViewById(R.id.iv_menember_head);
        this.tv_memember_name = (TextView) findViewById(R.id.tv_memember_name);
        this.tv_memember_height = (TextView) findViewById(R.id.tv_memember_height);
        this.tv_memember_weight = (TextView) findViewById(R.id.tv_memember_weight);
        this.tv_memember_position = (TextView) findViewById(R.id.tv_memember_position);
    }
    public void setTeam(Person person){
        this.tv_memember_height.setText(person.getHeight());
        this.tv_memember_weight.setText(person.getWeight());
        this.tv_memember_position.setText(person.getPosition());
    }

}
