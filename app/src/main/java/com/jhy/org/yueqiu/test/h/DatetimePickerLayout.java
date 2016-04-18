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
public class DatetimePickerLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_finish;

    private RelativeLayout rlay_year;
    private RelativeLayout rlay_month;
    private RelativeLayout rlay_day;
    private RelativeLayout rlay_hour;
    private RelativeLayout rlay_minute;
    private RelativeLayout rlay_second;

    private NumberPicker np_year;
    private NumberPicker np_month;
    private NumberPicker np_day;
    private NumberPicker np_hour;
    private NumberPicker np_minute;
    private NumberPicker np_second;
    private OnValuePickedListener pickedListener = null;

    public DatetimePickerLayout(Context context) { this(context, null); }

    public DatetimePickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_datetime_picker, this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        rlay_year = (RelativeLayout) findViewById(R.id.rlay_year);
        rlay_month = (RelativeLayout) findViewById(R.id.rlay_month);
        rlay_day = (RelativeLayout) findViewById(R.id.rlay_day);
        rlay_hour = (RelativeLayout) findViewById(R.id.rlay_hour);
        rlay_minute = (RelativeLayout) findViewById(R.id.rlay_minute);
        rlay_second = (RelativeLayout) findViewById(R.id.rlay_second);

        np_year = (NumberPicker) findViewById(R.id.np_year);
        np_month = (NumberPicker) findViewById(R.id.np_month);
        np_day = (NumberPicker) findViewById(R.id.np_day);
        np_hour = (NumberPicker) findViewById(R.id.np_hour);
        np_minute = (NumberPicker) findViewById(R.id.np_minute);
        np_second = (NumberPicker) findViewById(R.id.np_second);

        setNumberPicker();
        tv_finish.setOnClickListener(this);
    }

    private void setNumberPicker () {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        np_year.setMaxValue(year);
        np_year.setMinValue(year - 100);
        np_year.setValue(year - 20);

        np_month.setMaxValue(12);
        np_month.setMinValue(1);
        np_month.setValue(month);

        np_day.setMaxValue(31);
        np_day.setMinValue(1);
        np_day.setValue(day);

        np_hour.setMaxValue(23);
        np_hour.setMinValue(0);
        np_hour.setValue(hour);

        np_minute.setMaxValue(59);
        np_minute.setMinValue(0);
        np_minute.setValue(minute);

        np_second.setMaxValue(59);
        np_second.setMinValue(0);
        np_second.setValue(second);
    }

    public void setOnValuePickedListener (OnValuePickedListener pickedListener) {
        this.pickedListener = pickedListener;
    }

    public void setYearPickerVisible (boolean visible) {
        rlay_year.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setSecondPickerVisible (boolean visible) {
        rlay_second.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setDatePickerVisible (boolean visible) {
        int visibility = visible ? VISIBLE : GONE;
        rlay_year.setVisibility(visibility);
        rlay_month.setVisibility(visibility);
        rlay_day.setVisibility(visibility);
    }
    public void setTimePickerVisible (boolean visible) {
        int visibility = visible ? VISIBLE : GONE;
        rlay_hour.setVisibility(visibility);
        rlay_minute.setVisibility(visibility);
        rlay_second.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        if (pickedListener != null) {
            pickedListener.onValuePicked("");
        }
    }
}
