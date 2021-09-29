package com.saveeat.razorpay

import android.app.Activity
import org.json.JSONObject
import android.content.Context
import android.util.Log
import com.razorpay.Checkout
//import com.razorpay.Checkout
import com.saveeat.R
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import java.lang.Exception


class RazorPay {

   fun payload(context: Context?,order_id: String?, totalBill: Double?) {
       Log.e("totalBill", (totalBill?:0.0).toString())
       val checkout = Checkout()
        checkout.setKeyID("rzp_test_WnkDXfUKUg1dSQ")
       // checkout.setImage(R.drawable.app_icon_main)
        try {
            val options = JSONObject()

            val retryObj = JSONObject()
            retryObj.put("enabled", false)
            retryObj.put("max_count", 1)
            options.put("retry", retryObj)

            options.put("prefill.email", PrefrencesHelper.getPrefrenceStringValue(context!!,PreferenceKeyConstants.email))
            options.put("prefill.contact",PrefrencesHelper.getPrefrenceStringValue(context!!,PreferenceKeyConstants.countryCode)+""+ PrefrencesHelper.getPrefrenceStringValue(context!!,PreferenceKeyConstants.mobileNumber))

            options.put("name", context?.getString(R.string.app_name))
            options.put("description", "Online Food Ordering")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", totalBill?.times(100))



            //Order Id
            val jsonObject = JSONObject()
            jsonObject.put("order_id", order_id)
            options.put("notes", jsonObject)

            checkout.open(context as Activity?, options)
        } catch (e: Exception) {
          Log.e("a","a")
        }
    }
}