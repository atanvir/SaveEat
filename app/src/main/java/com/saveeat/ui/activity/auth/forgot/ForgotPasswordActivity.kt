package com.saveeat.ui.activity.auth.forgot

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityForgotPasswordBinding
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.next)
        authToolbar(this)
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{
                startActivity(Intent(this,OTPVerificationActivity::class.java).putExtra("type","forgot"))
            }
        }
    }
}