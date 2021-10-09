package com.saveeat.ui.dialog.picker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.ozcanalasalvar.library.view.datePicker.DatePicker.MONTH_ON_FIRST
import com.ozcanalasalvar.library.view.popup.DatePickerPopup
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(var binding: AdapterCartBinding, var data : CartDataModel?) : DialogFragment(), DatePickerPopup.OnDateSelectListener {

    val calender = Calendar.getInstance()
    val serverFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(serverFormat, Locale.US)
    var maxTime:Long?=0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")
        maxTime=calculateMaxDate()

       val mp= DatePickerPopup.Builder()
            .from(requireActivity())
            .textSize(19)
            .pickerMode(com.ozcanalasalvar.library.view.datePicker.DatePicker.DAY_ON_FIRST)
            .darkModeEnabled(true)
            .currentDate(calender?.timeInMillis?:0)
            .startDate(calender?.timeInMillis?:0)
            .endDate(maxTime?:0)
            .listener(this)
            .build()
       return mp
    }

    private fun calculateMaxDate(): Long {
        var date: Date?=null
        for(i in data?.productData?.indices!!) {
            if(data?.productData?.get(i)?.productDetail?.pickupLaterAllowance==true){
                val convertedDate: Date = sdf.parse(data?.productData?.get(i)?.productDetail?.convertedPickupDate)
                if(date==null){ date=convertedDate }
                if(date?.time?:0>convertedDate.time){ date=convertedDate } }
        }

        if(date!=null){
        Log.e("dateMax",""+date)
        val serverdate=Calendar.getInstance()
        serverdate.time=date
        return serverdate.timeInMillis
        }
        else return 0
    }



    override fun onDateSelected(dp: com.ozcanalasalvar.library.view.datePicker.DatePicker?, date: Long, day: Int, month: Int, year: Int) {
        binding.btnPickNow.text = requireActivity().getString(R.string.pick_up_now)
        val date= SimpleDateFormat("yyyy-MM-dd").parse("${year}-${month+1}-${day}")

        data?.pickupDate= SimpleDateFormat("yyyy-MM-dd").format(date)
        Log.e("date",data?.pickupDate?:"")
        data?.orderType="Later"
        dismiss()



        TimePickerFragment(binding,data,Date(maxTime!!)).show(requireActivity().supportFragmentManager,"")
    }
}