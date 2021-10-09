package com.saveeat.utils.extn

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


private var timer: CountDownTimer?=null
fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }
    })
}


fun EditText.onAfterChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            timer?.cancel()
            timer=null
            timer=  object :CountDownTimer(500,500){
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    listener(s.toString())
                }

            }
            timer?.start()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}


fun EditText.text(): String {
   return text.toString().trim()
}