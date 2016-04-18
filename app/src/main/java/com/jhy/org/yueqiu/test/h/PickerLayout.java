package com.jhy.org.yueqiu.test.h;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class PickerLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_finish;
    private NumberPicker np_picker;
    private boolean usesArray = true;
    private OnValuePickedListener pickedListener = null;

    public PickerLayout(Context context) { this(context, null); }
    public PickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_picker, this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        np_picker = (NumberPicker) findViewById(R.id.np_picker);

        tv_finish.setOnClickListener(this);
    }

    public void setTitle (String title) {
        tv_title.setText(title);
    }

    public void setValues (String[] values) {
        usesArray = true;
        np_picker.setDisplayedValues(values);
        np_picker.setMaxValue(values.length - 1);
    }

    public void setValues (int min, int max) {
        usesArray = false;
        np_picker.setMinValue(min);
        np_picker.setMaxValue(max);
    }

    public String getValue () {
        if (usesArray) {
            String[] values = np_picker.getDisplayedValues();
            return values[np_picker.getValue()];
        }
        return "" + np_picker.getValue();
    }

    public void setOnValuePickedListener (OnValuePickedListener pickedListener) {
        this.pickedListener = pickedListener;
    }

    @Override
    public void onClick(View v) {
        if (pickedListener != null) {
            pickedListener.onValuePicked(getValue());
        }
    }
}
