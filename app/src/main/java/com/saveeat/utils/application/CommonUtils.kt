package com.saveeat.utils.application

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.saveeat.R
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.drawer.help.HelpActivity
import com.saveeat.ui.activity.drawer.credit.CreditActivity
import com.saveeat.ui.activity.drawer.location.HiddenLocationActivity
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.ui.activity.drawer.payment.PaymentActivity
import com.saveeat.ui.activity.order.checkout.CheckoutActivity
import com.saveeat.utils.extn.onSelected


object CommonUtils {

    fun loadDrawable(context: Context, resourceId: Int): Drawable? {
        return context.getDrawable(resourceId)
    }

    fun getDeviceToken(context: Context): String {
//        var token:String?=null
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener<String?> { task ->
//            if (!task.isSuccessful) {
//                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//            token = task.result
//            context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(
//                PREF_DEVICE_TOKEN,
//                token
//            ).apply()
//        })

        return "token".toString()
    }

    fun authToolbar(activity: AppCompatActivity){
        val ivBack =activity.findViewById<ImageView>(R.id.ivBack)


        val clMainToolbar=activity.findViewById<ConstraintLayout>(R.id.toolbar)
        clMainToolbar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Log.e("height",""+clMainToolbar.height)
                Log.e("total Height",""+activity.resources.displayMetrics.heightPixels)
                val bottomSheet = activity.findViewById(R.id.clBottomSheet) as CoordinatorLayout
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = activity.resources.displayMetrics.heightPixels-(clMainToolbar.height+122)
                clMainToolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)            }
        })



        if(activity is LoginActivity){
            ivBack.visibility= View.GONE
        }
        ivBack.setOnClickListener{ activity.onBackPressed() }
    }

    fun toolbar(activity: AppCompatActivity){
        val ivBack =activity.findViewById<ImageView>(R.id.ivBack)
        val tvLabel =activity.findViewById<TextView>(R.id.tvLabel)
        if(activity is CreditActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Credits"
        }else if(activity is OrderHistoryActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Order Details & History"
        }else if(activity is HelpActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Help & Support"
        }else if(activity is HiddenLocationActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Unlock a hidden location"
        }else if(activity is PaymentActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Payment"
        }else if(activity is CheckoutActivity){
            tvLabel.visibility=View.VISIBLE
            tvLabel.text="Select Payment"
        }
        else{
            tvLabel.visibility=View.GONE
        }
        ivBack.setOnClickListener{ activity.onBackPressed() }
    }




    fun setSpinner(context: Context, list: Array<String?>, spinner: Spinner, textView: TextView) {
        val adapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(context, android.R.layout.simple_spinner_item,list) {
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
            view.background= ContextCompat.getDrawable(context,R.drawable.drawable_white_selected_tab_layout)
            view.setPadding(view.getPaddingLeft(), 16, view.getPaddingRight(), 16)
            return view
        }
        }

        spinner.onSelected { parent, position ->
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




}