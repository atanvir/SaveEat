package com.saveeat.ui.activity.auth.login.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityLoginWithOtpBinding
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginOTPModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.ui.activity.auth.forgot.ForgotViewModel
import com.saveeat.ui.activity.auth.login.LoginViewModel
import com.saveeat.ui.activity.auth.login.password.LoginWithPasswordActivity
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.ui.activity.auth.signup.SignUpActivity
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.PhoneNumberKit


@AndroidEntryPoint
class LoginWithOTPActivity : BaseActivity<ActivityLoginWithOtpBinding>(), View.OnClickListener {

    private var phoneNumberKit : PhoneNumberKit?=null
    private val viewModel : LoginViewModel by viewModels()
    override fun getActivityBinding(): ActivityLoginWithOtpBinding =ActivityLoginWithOtpBinding.inflate(layoutInflater)

    override fun inits() {
        binding.model= LoginOTPModel(mobileNumber = "",countryCode = "",
                                     deviceType = KeyConstants.DEVICE_TYPE,
                                     deviceToken = getSharedPreferences(KeyConstants.PREF_NAME, MODE_PRIVATE)?.getString(PreferenceKeyConstants.deviceToken,"").toString())
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.continues)
        CommonUtils.authToolbar(this)
        phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this as AppCompatActivity, searchEnabled = true)
    }

    override fun initCtrl() {
        binding.tvSignup.setOnClickListener(this)
        binding.tvLoginWithPassword.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.tiePhoneNo.addTextChangedListener { binding.tilPhoneNo.isErrorEnabled=false }
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.checkMobilForForgotPassword.observe(this@LoginWithOTPActivity, {
                CommonUtils.buttonLoader(binding.clShadowButton, false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            startActivity(Intent(this@LoginWithOTPActivity, OTPVerificationActivity::class.java).putExtra("data",binding?.model))
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                    }
                    is Resource.Failure -> {
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!)
                    }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvSignup ->{ startActivity(Intent(this, SignUpActivity::class.java)) }
            R.id.tvLoginWithPassword ->{ startActivity(Intent(this, LoginWithPasswordActivity::class.java)) }

            R.id.ivButton -> {
                CommonUtils.buttonLoader(binding.clShadowButton, true)
                if(checkValidation()){
                    binding?.model?.countryCode= CommonUtils.mobileNo(binding.tiePhoneNo)?.first?:""
                    binding?.model?.mobileNumber= CommonUtils.mobileNo(binding.tiePhoneNo)?.second?:""
                    viewModel.forgot(binding.model)
                }else{
                    CommonUtils.buttonLoader(binding.clShadowButton, false)
                }
            }
        }
    }


    private fun checkValidation() : Boolean{
        var ret=true
        if(CommonUtils.mobileNo(binding.tiePhoneNo)?.second.isNullOrEmpty()){
            ret=false
            binding.tilPhoneNo.isErrorEnabled=true
            binding.tilPhoneNo.error=getString(R.string.please_enter_phone_number)

        }else if(phoneNumberKit?.isValid==false){
            ret=false
            binding.tilPhoneNo.isErrorEnabled=true
            binding.tilPhoneNo.error=getString(R.string.please_enter_valid_phone_number)
        }

        return ret

    }

    override fun onBackPressed() {
        finishAffinity()
    }
}