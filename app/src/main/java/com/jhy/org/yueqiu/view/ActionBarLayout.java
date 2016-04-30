package com.jhy.org.yueqiu.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;


public class ActionBarLayout extends RelativeLayout implements View.OnClickListener {
    private Activity activity;
    private ImageButton ibtn_back;

    private ImageView img_logo;
    private TextView tv_titile;

    private ImageButton ibtn_options;

    public ActionBarLayout(Context context) {
        this(context, null);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_action_bar, this);
        activity = (Activity) context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.actionbar);
        initView(context, typedArray);
        typedArray.recycle();
    }

    private void initView (Context context, TypedArray typedArray) {
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(this);

        Drawable logo_src = typedArray.getDrawable(R.styleable.actionbar_logo_src);
        boolean logo_visible = typedArray.getBoolean(R.styleable.actionbar_logo_visible, false);

        img_logo = (ImageView) findViewById(R.id.img_logo);
        if (logo_src != null) {
            img_logo.setImageDrawable(logo_src);
            img_logo.setVisibility(VISIBLE);
        } else {
            img_logo.setVisibility(logo_visible ? VISIBLE : GONE);
        }

        String title_text = typedArray.getString(R.styleable.actionbar_title_text);
        float title_textSize = typedArray.getDimension(R.styleable.actionbar_title_textSize, 20);
        tv_titile = (TextView) findViewById(R.id.tv_title);
        tv_titile.setText(title_text == null ? "" : title_text);
        tv_titile.setTextSize(title_textSize);

        Drawable options_src = typedArray.getDrawable(R.styleable.actionbar_options_src);
        boolean options_visible = typedArray.getBoolean(R.styleable.actionbar_options_visible, false);

        ibtn_options = (ImageButton) findViewById(R.id.ibtn_options);
        if (options_src != null) {
            ibtn_options.setImageDrawable(options_src);
            ibtn_options.setVisibility(VISIBLE);
        } else {
            ibtn_options.setVisibility(options_visible ? VISIBLE : GONE);
        }
    }

    public void setTitleText (CharSequence text) {
        tv_titile.setText(text);
    }

    public void setOptionsOnClickListener (OnClickListener listener) {
        ibtn_options.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.ibtn_back) {
            activity.finish();
        }
    }
}
