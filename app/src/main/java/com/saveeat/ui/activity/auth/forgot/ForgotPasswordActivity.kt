package com.saveeat.ui.activity.auth.forgot

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityForgotPasswordBinding
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.repository.cache.DataStore
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.auth.login.LoginViewModel
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.mobileNo
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import me.ibrahimsn.lib.PhoneNumberKit

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(), View.OnClickListener {
    private var phoneNumberKit : PhoneNumberKit?=null
    private val viewModel : ForgotViewModel by viewModels()



    override fun getActivityBinding(): ActivityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)

    override fun inits() {
        binding.model= ForgotModel(countryCode = "",mobileNumber = "",userId = "",password = "")
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.next)
        authToolbar(this)
        phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this as AppCompatActivity, searchEnabled = true)
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.tiePhoneNo.addTextChangedListener { binding.tilPhoneNo.isErrorEnabled=false }
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.checkMobilForForgotPassword.observe(this@ForgotPasswordActivity, {
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding?.model?.userId=it.value?.data?.userId?:""
                            startActivity(Intent(this@ForgotPasswordActivity, OTPVerificationActivity::class.java).putExtra("data",binding?.model))
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
            R.id.ivButton -> {
                buttonLoader(binding.clShadowButton,true)
                if(checkValidation()){
                    binding?.model?.countryCode= mobileNo(binding.tiePhoneNo)?.first?:""
                    binding?.model?.mobileNumber= mobileNo(binding.tiePhoneNo)?.second?:""
                    viewModel.forgot(binding.model)
                }else{
                    buttonLoader(binding.clShadowButton,false)
                }
            }
        }
    }

    private fun checkValidation() : Boolean{
        var ret=true
        if(mobileNo(binding.tiePhoneNo)?.second.isNullOrEmpty()){
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
}