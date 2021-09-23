package com.saveeat.ui.activity.order.status

import android.content.Intent
import android.graphics.Color
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityOrderStatusBinding
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.extn.roundOffDecimal
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Handler

@AndroidEntryPoint
class OrderStatusActivity : BaseActivity<ActivityOrderStatusBinding>() {
    override fun getActivityBinding(): ActivityOrderStatusBinding = ActivityOrderStatusBinding.inflate(layoutInflater)

    override fun inits() {
        val wordtoSpan: Spannable = SpannableString("Congrats! you have saved ${getString(R.string.price,intent.getStringExtra("saveAmount"))}")
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(0, 178, 17)), 24, wordtoSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        CommonUtils.increaseFontSizeForPath(wordtoSpan, "${getString(R.string.price,intent.getStringExtra("saveAmount"))}", 1.25f)
        binding.tvSaveAmount.text = wordtoSpan


        android.os.Handler(Looper.getMainLooper()).postDelayed(Runnable {
          startActivity(Intent(this,OrderHistoryActivity::class.java).putExtra("cart",true))
          finish()
        },4000)
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

    override fun onBackPressed() {

    }

}