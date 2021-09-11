package com.saveeat.ui.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import com.saveeat.model.response.saveeat.menu.TodayDataModel
import com.saveeat.utils.application.ErrorUtil
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment(var binding: AdapterCartBinding, var data : CartDataModel?) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var dateTimeFormat=SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var weekDateFormat=SimpleDateFormat("EEEE")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(activity, this, hour, minute, android.text.format.DateFormat.is24HourFormat(activity))
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

        if(date<=Calendar.getInstance().time){
            ErrorUtil.snackView(binding.root,"Please select valid time")
        }
        else if(date>=times?.first && date<=times?.second){
            data?.pickupTime= SimpleDateFormat("HH:mm").format(date)
            binding.btnPickLater.text=data?.pickupTime

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