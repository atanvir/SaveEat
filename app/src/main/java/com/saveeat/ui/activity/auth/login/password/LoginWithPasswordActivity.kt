package com.saveeat.ui.activity.auth.login.password

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityLoginBinding
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.repository.cache.PreferenceKeyConstants.deviceToken
import com.saveeat.repository.cache.PreferenceKeyConstants.walkthrow
import com.saveeat.repository.cache.PrefrencesHelper.saveUserData
import com.saveeat.ui.activity.auth.forgot.ForgotPasswordActivity
import com.saveeat.ui.activity.auth.login.LoginViewModel
import com.saveeat.ui.activity.auth.signup.SignUpActivity
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.isValidPassword
import com.saveeat.utils.application.CommonUtils.mobileNo
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.DEVICE_TYPE
import com.saveeat.utils.application.KeyConstants.PASSWORD_VALIDATION
import com.saveeat.utils.application.KeyConstants.PREF_NAME
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import me.ibrahimsn.lib.PhoneNumberKit

@AndroidEntryPoint
class LoginWithPasswordActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {
    private var phoneNumberKit : PhoneNumberKit?=null
    private val viewModel : LoginViewModel by viewModels()


    override fun getActivityBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun inits() {
        authToolbar(this)
        binding.model= LoginModel(countryCode = "",mobileNumber = "",
                                  password = "",deviceType = DEVICE_TYPE,
                                  deviceToken = getSharedPreferences(PREF_NAME, MODE_PRIVATE)?.getString(deviceToken,"").toString())

        phoneNumberKit = PhoneNumberKit(this)
        phoneNumberKit?.attachToInput(binding.tilPhoneNo, binding.countryCodePicker.selectedCountryCodeAsInt)
        phoneNumberKit?.setupCountryPicker(this as AppCompatActivity, searchEnabled = true)

        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putBoolean(walkthrow,true).apply()
    }

    override fun initCtrl() {
        binding.tvSignup.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.tiePhoneNo.addTextChangedListener { binding.tilPhoneNo.isErrorEnabled=false }
        binding.tiePassword.addTextChangedListener { binding.tilPassword.isErrorEnabled=false }
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.userLogin.observe(this@LoginWithPasswordActivity, {
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            saveUserData(this@LoginWithPasswordActivity,it.value?.data)
                            startActivity(Intent(this@LoginWithPasswordActivity,MainActivity::class.java))
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
        when(v?.id) {
            R.id.tvSignup ->{ startActivity(Intent(this,SignUpActivity::class.java)) }
            R.id.tvForgotPassword ->{ startActivity(Intent(this,ForgotPasswordActivity::class.java)) }
            R.id.ivButton ->{
            buttonLoader(binding.clShadowButton,true)
            if(checkValidation()){
                binding?.model?.mobileNumber=mobileNo(binding?.tiePhoneNo)?.second?:""
                binding?.model?.countryCode=mobileNo(binding?.tiePhoneNo)?.first?:""
                viewModel.login(binding?.model)
            }else{
                buttonLoader(binding.clShadowButton,false)
            } }
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
        }else if(binding?.model?.password?.isEmpty()==true){
            ret=false
            binding.tilPassword.isErrorEnabled=true
            binding.tilPassword.error=getString(R.string.enter_password)

        }
        else if((isValidPassword(binding?.model?.password)==false) || binding?.model?.password?.length?:0< PASSWORD_VALIDATION){
            ret=false
            binding.tilPassword.isErrorEnabled=true
            binding.tilPassword.error = getString(R.string.enter_password_character)
        }

        return ret
    }


    override fun onBackPressed() {
        finishAffinity()
    }


}