package com.saveeat.ui.activity.order.checkout

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityCheckoutBinding
import com.saveeat.ui.activity.drawer.history.OrderHistoryActivity
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityCheckoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.clShadowButton.tvButtonLabel.text="Checkout"
    }

    override fun initCtrl() {
        binding.btnPay.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.ivButton ->{ startActivity(Intent(this,OrderHistoryActivity::class.java)) }
           R.id.btnPay ->{ binding.clShadowButton.ivButton.performClick() }
       }
    }
}