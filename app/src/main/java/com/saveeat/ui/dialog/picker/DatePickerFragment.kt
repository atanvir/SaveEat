package com.saveeat.ui.dialog.picker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.saveeat.R
import com.saveeat.databinding.AdapterCartBinding
import com.saveeat.model.response.saveeat.cart.CartDataModel
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(var binding: AdapterCartBinding, var data : CartDataModel?) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDailog=DatePickerDialog(requireActivity(),AlertDialog.THEME_HOLO_LIGHT, this, year, month, day)
        datePickerDailog.datePicker?.minDate=c.timeInMillis
        datePickerDailog.datePicker?.maxDate=c.timeInMillis+(7* 24 * 60 * 60 * 1000)
        return datePickerDailog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        binding.btnPickNow.text = requireActivity().getString(R.string.pick_up_now)
        val date= SimpleDateFormat("yyyy-MM-dd").parse("${year}-${month+1}-${day}")

        data?.pickupDate= SimpleDateFormat("yyyy-MM-dd").format(date)
        Log.e("date",data?.pickupDate?:"")
        data?.orderType="Later"
        dismiss()
        TimePickerFragment(binding,data).show(requireActivity().supportFragmentManager,"")
    }
}