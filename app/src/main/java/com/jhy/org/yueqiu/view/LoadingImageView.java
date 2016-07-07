package com.jhy.org.yueqiu.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jhy.org.yueqiu.R;

public class LoadingImageView extends ImageView {
    private AnimationDrawable animationDrawable = null;

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView () {
        setBackgroundResource(R.drawable.anim_loading);
    }

    private AnimationDrawable getAnimationDrawable () {
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) getBackground();
        }
        return animationDrawable;
    }

    public void show () {
        getAnimationDrawable().start();
        setVisibility(VISIBLE);
    }

    public void hide () {
        getAnimationDrawable().stop();
        setVisibility(INVISIBLE);
    }
}
