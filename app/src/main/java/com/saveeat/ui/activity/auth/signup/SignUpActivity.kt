package com.saveeat.ui.activity.auth.signup

import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivitySignUpBinding
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PreferenceKeyConstants.deviceToken
import com.saveeat.ui.activity.auth.otp.OTPVerificationActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.mobileNo
import com.saveeat.utils.application.ErrorUtil.handlerGeneralError
import com.saveeat.utils.application.ErrorUtil.snackView
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.DEVICE_TYPE
import com.saveeat.utils.application.KeyConstants.PREF_NAME
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.PhoneNumberKit

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(), View.OnClickListener {
    private var phoneNumberKit : PhoneNumberKit?=null

    private val viewModel : SignupViewModel by viewModels()
    override fun getActivityBinding(): ActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

    override fun inits() {

        binding.model= SignupModel(password = "",name="",email = "",countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus,
                                   mobileNumber = "",deviceType = DEVICE_TYPE,deviceToken = getSharedPreferences(PREF_NAME, MODE_PRIVATE)?.getString(deviceToken,"").toString(),
                                   address = "",latitude = 0.0,longitude = 0.0,distance="0")

        binding.clShadowButton.tvButtonLabel.text=getString(R.string.sign_up)
        authToolbar(this)

        phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this as AppCompatActivity, searchEnabled = true)
    }

    override fun initCtrl() {
        binding.apply {
            tvLogin.setOnClickListener(this@SignUpActivity)
            clShadowButton.ivButton.setOnClickListener(this@SignUpActivity)
            tieFullName.addTextChangedListener { tilFullName.isErrorEnabled=false }
            tieEmail.addTextChangedListener { tilEmail.isErrorEnabled=false }
            tiePhoneNo.addTextChangedListener { tilPhoneNo.isErrorEnabled=false }
        }
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.checkUserMobileAndEmail.observe(this@SignUpActivity, {
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding?.model?.countryCode= mobileNo(binding.tiePhoneNo)?.first
                            binding?.model?.mobileNumber= mobileNo(binding.tiePhoneNo)?.second
                            startActivity(Intent(this@SignUpActivity,OTPVerificationActivity::class.java).putExtra("data",binding.model))
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) snackView(binding.root,it.value?.message?:"")
                    }
                    is Resource.Failure -> { handlerGeneralError(binding.root,it.throwable!!) }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvLogin ->{ finish() }
            R.id.ivButton ->{
                if(checkValidation()){
                    buttonLoader(binding.clShadowButton,true)
                    viewModel.checkUserMobileAndEmail(email=binding?.model?.email, mobileNo(binding?.tiePhoneNo)?.second)
                }
            }
        }
    }



    private fun checkValidation() : Boolean {
        var ret=true
        if(binding?.model?.name?.trim().isNullOrEmpty()) {
            ret=false
            binding.tilFullName.isErrorEnabled=true
            binding.tilFullName.error=getString(R.string.please_enter_full_name)

        }else if(binding?.model?.email?.trim().isNullOrEmpty()){
            ret=false
            binding.tilEmail.isErrorEnabled=true
            binding.tilEmail.error=getString(R.string.please_enter_emailId)
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding?.model?.email?.trim()?:"").matches()){
            ret=false
            binding.tilEmail.isErrorEnabled=true
            binding.tilEmail.error=getString(R.string.valid_email_id)
        }else if(mobileNo(binding.tiePhoneNo)?.second.isNullOrEmpty()){
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
        super.onBackPressed()
    }

}