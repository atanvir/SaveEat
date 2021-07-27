package com.saveeat.ui.activity.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivitySignUpBinding
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.PhoneNumberKit

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

    override fun inits() {
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.sign_up)
        authToolbar(this)

        val phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this as AppCompatActivity, searchEnabled = true)
    }

    override fun initCtrl() {
        binding.tvLogin.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvLogin ->{ startActivity(Intent(this,LoginActivity::class.java)) }
            R.id.ivButton ->{ startActivity(Intent(this,OTPVerificationActivity::class.java).putExtra("type","signup")) }
        }
    }

}