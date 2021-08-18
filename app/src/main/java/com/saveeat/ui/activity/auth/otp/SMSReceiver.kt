package com.saveeat.ui.activity.auth.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("receive","yes")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extras = intent.extras
            val status: Status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val msgs: String = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.e("current Message",msgs)
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.e("timeout","yes")
                }
            }
        }
    }
}