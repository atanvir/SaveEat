package com.saveeat.utils.application;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTimePicker extends TimePickerDialog {

    public static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;



    public CustomTimePicker(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
//        AlertDialog.THEME_HOLO_DARK
//        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
//        AlertDialog.THEME_HOLO_LIGHT
//       AlertDialog.THEME_DEVICE_DEFAULT_DARK
//         AlertDialog.THEME_TRADITIONAL
        super(context,  AlertDialog.THEME_HOLO_LIGHT,callBack, hourOfDay, minute, is24HourView);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        super.onTimeChanged(timePicker, hourOfDay, minute);
        if (!mIgnoreEvent){
            minute = getRoundedMinute(minute);
            mIgnoreEvent=true;
            timePicker.setCurrentMinute(minute);
            mIgnoreEvent=false;
        }
    }

    public static int getRoundedMinute(int minute){
        if(minute % TIME_PICKER_INTERVAL != 0){
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)  minute=0;
        }

        return minute;
    }

}
