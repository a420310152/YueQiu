package com.jhy.org.yueqiu.test.h;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.view.OnValuePickedListener;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class DatePickerLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_finish;
    private NumberPicker np_year;
    private NumberPicker np_month;
    private NumberPicker np_day;
    private OnValuePickedListener pickedListener = null;

    public DatePickerLayout(Context context) { this(context, null); }

    public DatePickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_date_picker, this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        np_year = (NumberPicker) findViewById(R.id.np_year);
        np_month = (NumberPicker) findViewById(R.id.np_month);
        np_day = (NumberPicker) findViewById(R.id.np_day);

        setNumberPicker();
        tv_finish.setOnClickListener(this);
    }

    private void setNumberPicker () {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        np_year.setMaxValue(year);
        np_year.setMinValue(year - 100);
        np_year.setValue(year-20);

        np_month.setMaxValue(12);
        np_month.setMinValue(1);
        np_month.setValue(month);

        np_day.setMaxValue(31);
        np_day.setMinValue(1);
        np_day.setValue(day);
    }

    public void setOnValuePickedListener (OnValuePickedListener pickedListener) {
        this.pickedListener = pickedListener;
    }

    @Override
    public void onClick(View v) {
        if (pickedListener != null) {
            pickedListener.onValuePicked("");
        }
    }
}
