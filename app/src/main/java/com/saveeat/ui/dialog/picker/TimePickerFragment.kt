package com.saveeat.ui.dialog.picker

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import com.saveeat.utils.application.ErrorUtil
import java.text.SimpleDateFormat
import java.util.*
import com.saveeat.utils.application.CustomTimePicker

class TimePickerFragment(var binding: AdapterCartBinding, var data : CartDataModel?,var maxTime: Date?) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var dateTimeFormat=SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private var weekDateFormat=SimpleDateFormat("EEEE", Locale.US)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dateTimeFormat.timeZone = TimeZone.getTimeZone("GMT+05:30")
        weekDateFormat.timeZone = TimeZone.getTimeZone("GMT+05:30")


        var timePicker=CustomTimePicker(activity,this, Calendar.getInstance().get(Calendar.HOUR), CustomTimePicker.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePicker.TIME_PICKER_INTERVAL), false)
        return timePicker
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val date= dateTimeFormat.parse("${data?.pickupDate} ${hourOfDay}:${minute}")
        loadTime(date,weekDateFormat.format(date))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.btnPickNow.backgroundTintList=context?.getColorStateList(R.color.white)
            binding.btnPickNow.setTextColor(context?.getColor(R.color.app_theme)!!)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.btnPickLater.backgroundTintList=context?.getColorStateList(R.color.app_theme)
            binding.btnPickLater.setTextColor(context?.getColor(R.color.white)!!)
        }

        dismiss()
    }

    private fun loadTime(date : Date,day: String) {
        val times=selectedTime(data?.timeData,day)

        var newFormat=SimpleDateFormat("hh:mm")

        if(date<=Calendar.getInstance().time){
            ErrorUtil.snackView(binding.root,"Please select valid time")
        }else if(newFormat.format(times?.first!!)>=newFormat.format(maxTime) && newFormat.format(times?.second!!) <= newFormat.format(maxTime)){
            ErrorUtil.snackView(binding.root,"Sorry restaurant is closed ,Please try same order with another restaurant")
        }else if(date>=maxTime){
            ErrorUtil.snackView(binding.root,"Sorry! pick up time for this item is exceeded")
        }
        else if(date>=times?.first && date<=times?.second){
            data?.pickupTime= SimpleDateFormat("HH:mm").format(date)
            binding.btnPickLater.text=SimpleDateFormat("dd/MM/yy h:mm a").format(date)

        }else{
            ErrorUtil.snackView(binding.root,"Please select time between ${dateTimeFormat.format(times?.first)} to ${dateTimeFormat.format(times?.second)} for ${day}")
        }

    }

    private fun selectedTime(todayDataModel: MutableList<TodayDataModel?>?, day: String) : Pair<Date?,Date?>? {

        var pair : Pair<Date?,Date?>?=Pair(Calendar.getInstance().time,Calendar.getInstance().time)

        for(i in todayDataModel?.indices!!)
            if(todayDataModel?.get(i)?.day?.equals(day,ignoreCase = true)==true){
             pair=Pair(dateTimeFormat.parse("${data?.pickupDate} ${todayDataModel[i]?.pickupWindowOpen}"),dateTimeFormat.parse("${data?.pickupDate} ${todayDataModel[i]?.pickupWindowClose}"))
            break
        }

        return pair
    }
}