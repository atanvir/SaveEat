package com.saveeat.ui.activity.drawer.payment

import android.view.View
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityPaymentBinding
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityPaymentBinding = ActivityPaymentBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
        }
    }
}