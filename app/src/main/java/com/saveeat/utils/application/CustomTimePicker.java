package com.saveeat.utils.application;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomTimePicker extends TimePickerDialog {

    public int TIME_PICKER_INTERVAL=15;
    private TimePicker mTimePicker;

    public CustomTimePicker(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context,  AlertDialog.THEME_HOLO_LIGHT,callBack, hourOfDay, minute, is24HourView);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }


}

