package com.saveeat.utils.application

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import com.saveeat.R
import com.saveeat.databinding.LayoutCommonButtonBinding
import com.saveeat.model.response.saveeat.cart.ProductDataModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.ui.activity.auth.login.password.LoginWithPasswordActivity
import com.saveeat.ui.activity.auth.password.PasswordActivity
import com.saveeat.ui.activity.drawer.help.HelpActivity
import com.saveeat.ui.activity.drawer.credit.CreditActivity
import com.saveeat.ui.activity.drawer.location.HiddenLocationActivity
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.ui.activity.drawer.payment.PaymentActivity
import com.saveeat.ui.activity.order.checkout.CheckoutActivity
import com.saveeat.utils.extn.onSelected
import com.saveeat.utils.extn.text
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.*
import com.saveeat.repository.cache.PreferenceKeyConstants.deviceToken
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.activity.auth.login.otp.LoginWithOTPActivity
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.ui.activity.drawer.content.StaticContentActivity
import com.saveeat.ui.activity.filter.FilterActivity
import com.saveeat.ui.activity.filter.FilterCategoryActivity
import com.saveeat.ui.activity.filter.FilterResultActivity
import com.saveeat.ui.activity.rating.RatingActivity
import com.saveeat.ui.dialog.error.ErrorDialog

import com.saveeat.utils.application.KeyConstants.PREF_NAME
import com.saveeat.utils.extn.enable
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object CommonUtils {

    fun loadDrawable(context: Context, resourceId: Int): Drawable? {
        return context.getDrawable(resourceId)
    }

    fun generateFCMToken(context: Context?) : String? {
        var fcmToken : String?=null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                generateFCMToken(context)
                return@OnCompleteListener
            }

            fcmToken = task.result
            context?.getSharedPreferences(PREF_NAME, MODE_PRIVATE)?.edit()?.putString(deviceToken,fcmToken)?.apply()

        })
    return fcmToken
    }

    fun increaseFontSizeForPath(spannable: Spannable, path: String, increaseTime: Float) {
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(RelativeSizeSpan(increaseTime), startIndexOfPath, startIndexOfPath + path.length, 0)
    }


    fun authToolbar(activity: AppCompatActivity){
        val ivBack =activity.findViewById<ImageView>(R.id.ivBack)
        var clMainToolbar : ConstraintLayout?=null
        if(activity is PasswordActivity) clMainToolbar=activity.findViewById<ConstraintLayout>(R.id.toolbars)
        else if(activity is OTPVerificationActivity) clMainToolbar=activity.findViewById<ConstraintLayout>(R.id.toolbarOTP)
        else clMainToolbar=activity.findViewById<ConstraintLayout>(R.id.toolbar)

        clMainToolbar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Log.e("height",""+clMainToolbar.height)
                Log.e("total Height",""+activity.resources.displayMetrics.heightPixels)
                val bottomSheet = activity.findViewById(R.id.clBottomSheet) as CoordinatorLayout
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = activity.resources.displayMetrics.heightPixels-(clMainToolbar.height+122)
                clMainToolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)            }
        })



        if(activity is LoginWithOTPActivity){
            ivBack.visibility= View.GONE
        }
        ivBack.setOnClickListener{ activity.onBackPressed() }
    }

    fun toolbar(activity: AppCompatActivity){
        val ivBack =activity.findViewById<ImageView>(R.id.ivBack)
        val tvLabel =activity.findViewById<TextView>(R.id.tvLabel)
        if(activity is CreditActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.credits)
        }else if(activity is OrderHistoryActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.order_details_amp_history)
        }else if(activity is HelpActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.help_amp_support)
        }else if(activity is HiddenLocationActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.unlock_hidden_location)
        }else if(activity is PaymentActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.payment)
        }else if(activity is CheckoutActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.select_payment)
        }else if(activity is RatingActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.review_rating)
        }else if(activity is FilterActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.filter)
        }else if(activity is FilterCategoryActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.intent.getStringExtra("name")
        }else if(activity is StaticContentActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.intent.getStringExtra("type")
        }else if(activity is FilterResultActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text=activity.getString(R.string.filter_result)
        }
        else{
            tvLabel.visibility=View.GONE
        }
        ivBack.setOnClickListener{ activity.onBackPressed() }
    }




    fun setSpinner(context: Context,  spinner: Spinner, textView: TextView) {
        val adapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(context, android.R.layout.simple_spinner_item,
                                                                            arrayOf<String?>(context.getString(R.string.please_select_distance),
                                                                            "Within 0.5 KM","Within 1 KM",
                                                                            "Within 1.5 KM","Within 2 KM","Within 2.5 KM","Within 3 KM","Within 3.5 KM","Within 4 KM","Within 4.5 KM","Within 5 KM")) {
        override fun isEnabled(position: Int): Boolean {
            return position!=0
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getDropDownView(position?:0, convertView, parent)
            val tv = view as TextView
            if (position == 0) tv.setTextColor(ContextCompat.getColor(context,R.color.grey))
            else tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            tv.textSize = 14f

            val face = ResourcesCompat.getFont(context, R.font.poppins_regular)
            tv.typeface = face
            tv.textAlignment= View.TEXT_ALIGNMENT_CENTER
            view.background= ContextCompat.getDrawable(context,R.drawable.drawable_spinner)
            view.setPadding(view.getPaddingLeft(), 16, view.getPaddingRight(), 16)
            return view
        }
        }

        spinner.onSelected { parent, position ->
            if(position>0){
                if(parent?.getItemAtPosition(position).toString().contains("Mtr")){
                    textView.tag=parent?.getItemAtPosition(position).toString().split("Within ")[1].split("Mtr")[0]
                }else if(parent?.getItemAtPosition(position).toString().contains("KM")){
                    textView.tag=parent?.getItemAtPosition(position).toString().split("Within ")[1].split("KM")[0]
                }else if(parent?.getItemAtPosition(position).toString().contains("KMS")){
                    textView.tag=parent?.getItemAtPosition(position).toString().split("Within ")[1].split("KMS")[0]
                }
            }
            textView.text=parent?.getItemAtPosition(position).toString()
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = adapter
   }

    fun isOnline(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (mobile_info != null) {
                mobile_info.isConnectedOrConnecting || wifi_info!!.isConnectedOrConnecting
            } else {
                wifi_info!!.isConnectedOrConnecting
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("" + e)
            false
        }
    }

    fun buttonLoader(binding: LayoutCommonButtonBinding?, loader: Boolean) {
        if(loader){
            binding?.ivButton?.enable(false)
            binding?.tvButtonLabel?.visibility=View.GONE
            binding?.pbLoader?.visibility=View.VISIBLE
        }else{
            binding?.ivButton?.enable(true)
            binding?.tvButtonLabel?.visibility=View.VISIBLE
            binding?.pbLoader?.visibility=View.GONE
        }
    }

    fun mobileNo(mobileNo: TextInputEditText) : Pair<String,String>? {
        val countryCode=getCountryIsoCode(mobileNo.text(), mobileNo.context)?:""
        val no= "+"+mobileNo.text().replace("[^\\d]".toRegex(), "")
        val returnMobileNo =no.replace(countryCode, "",ignoreCase = true)
        return  Pair<String,String>(countryCode,returnMobileNo)
    }



    private fun getCountryIsoCode(number: String, context: Context): String? {
        val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
        val validatedNumber = if (number.startsWith("+")) number else "+$number"
        val phoneNumber = try {
            phoneNumberUtil.parse(validatedNumber, null)
        } catch (e: NumberParseException) {
            Log.e("TAG", "error during parsing a number")
            null
        } ?: return ""
        return "+"+phoneNumber.countryCode
    }

    fun TAG(context: Context): String? {
        return (context as AppCompatActivity)::class.simpleName
    }
    public fun openGoogleMap(context: Context, latitute: Double?, longtitute: Double?) {
        if(context!=null) {
            val builder: Uri.Builder = Uri.Builder()
            builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin", getPrefrenceStringValue(context, PreferenceKeyConstants.latitude) + "," + getPrefrenceStringValue(context, PreferenceKeyConstants.longitude))
                .appendQueryParameter("destination", latitute.toString() + "," + longtitute.toString())
            val url: String = builder.build().toString()
            Log.d("Directions", url)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }

     fun convertToBitmap(drawable: Drawable?, widthPixels: Int, heightPixels: Int): Bitmap? {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable?.setBounds(0, 0, widthPixels, heightPixels)
        drawable?.draw(canvas)
        return com.saveeat.utils.application.CommonUtils.getRoundedCornerBitmap(mutableBitmap,100)
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun isValidPassword(password: String?): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$");
        return passwordREGEX.matcher(password).matches()
    }

    fun createDrawableFromView(context: Context?, view: View?): Bitmap? {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        view?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view?.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view?.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view?.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view?.measuredWidth?:0, view?.measuredHeight?:0, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view?.draw(canvas)
        return bitmap
    }



    fun getProductType(sellingStatus: Boolean?): String? {
       return when(sellingStatus){
           true -> "Selling"
           else -> "Fixed"
       }

    }

    fun calculateCombination(min: Int?, max: Int?) : String? {
        if(min?:0==0 && max?:0==0) return "(Optional)"
        else if(min?:0==0 && max?:0<2) return "(Choose upto $max)"
        else if(min?:0==0 && max?:0>=2) return "(Choose upto $max)"
        else if(min?:0>0){
            val remainder=((max?:0) - (min?:0))
            when {
                remainder>=2  ->  { return "(Choose from $min to $max)" }
                else -> { return "(Choose $max)" }
            }
        }
        return "(Not a valid combination)"
    }

    fun calculateCombinationValidation(min: Int?, max: Int?) : String? {
        if(min?:0==0 && max?:0==0) return "(Optional)"
        else if(min?:0==0 && max?:0<2) return " upto $max"
        else if(min?:0==0 && max?:0>=2) return " upto $max"
        else if(min?:0>0){
            val remainder=((max?:0) - (min?:0))
            when {
                remainder>=2  ->  { return " min $min to max $max" }
                else -> { return " max $max" }
            }
        }
        return "(Not a valid combination)"
    }

    fun showDialog(context: Context, message: String?) {
        val dialog = ErrorDialog()
        val bundle = Bundle()
        bundle.putString("message", message?:"")
        dialog.arguments = bundle
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, com.saveeat.R.style.Dialog_NoTitle);
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    fun calculateDate(date: Date): String? {
        return  SimpleDateFormat("dd-MM-yyyy").format(date)
    }


    fun calculateTime(date: Date): String? {
        return  SimpleDateFormat("hh:mm a").format(date)
    }

    fun calculateMaxDate(data : MutableList<ProductDataModel?>?): Long {

        val serverFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val sdf = SimpleDateFormat(serverFormat, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")

        var date: Date?=null
        for(i in data?.indices!!) {
            if(data?.get(i)?.productDetail?.pickupLaterAllowance==true){
                val convertedDate: Date = sdf.parse(data?.get(i)?.productDetail?.convertedPickupDate)

                if(date==null){ date=convertedDate }
                if(date?.time?:0>convertedDate.time){ date=convertedDate } }
        }
        val serverdate=Calendar.getInstance()
        if(date!=null){
            serverdate.time=date
            return serverdate.timeInMillis
        }
        else{ return 0 }
    }

    fun loadTermSpannable(context: Context?) : Spannable {
        val wordtoSpan: Spannable = SpannableString("I accept all Terms & Conditions and Privacy Policy")
        wordtoSpan.setSpan(object : ClickableSpan() { override fun onClick(vi: View) {
            context?.startActivity(Intent(context, StaticContentActivity::class.java).putExtra("type",PreferenceKeyConstants.term_condition))
        } override fun updateDrawState(ds: TextPaint) { super.updateDrawState(ds) } }, 12, 31, 0)
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(255,122,44)), 12, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)



        wordtoSpan.setSpan(object : ClickableSpan() { override fun onClick(vi: View) {
            context?.startActivity(Intent(context, StaticContentActivity::class.java).putExtra("type",PreferenceKeyConstants.privacy_policy))
        } override fun updateDrawState(ds: TextPaint) { super.updateDrawState(ds) } }, 32, wordtoSpan?.length, 0)
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(255,122,44)), 36, wordtoSpan?.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(object : ClickableSpan() { override fun onClick(vi: View) {
        } override fun updateDrawState(ds: TextPaint) { super.updateDrawState(ds)
            ds.isUnderlineText=false } }, 32, 35, 0)
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(0,0,0)),32, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return wordtoSpan
    }
}