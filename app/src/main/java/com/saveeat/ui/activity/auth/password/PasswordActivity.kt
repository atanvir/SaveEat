package com.saveeat.ui.activity.auth.password

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityPasswordBinding
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.ui.activity.auth.login.password.LoginWithPasswordActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.isValidPassword
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.PASSWORD_VALIDATION
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.text
import com.saveeat.utils.extn.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordActivity : BaseActivity<ActivityPasswordBinding>(), View.OnClickListener {
    var data: Any?=null
    private val viewModel : PasswordViewModel by viewModels()

    override fun getActivityBinding(): ActivityPasswordBinding = ActivityPasswordBinding.inflate(layoutInflater)

    override fun inits() {
       authToolbar(this)
        data=intent?.getParcelableExtra("data")
        if(data is SignupModel){
            binding.clShadowButton.tvButtonLabel.text=getString(R.string.save)
        }else if(data is ForgotModel){
            binding.tvTitleLabel.text=getString(R.string.reset_password)
            binding.tvDescLabel.text=getString(R.string.enter_new_password)
            binding.clShadowButton.tvButtonLabel.text=getString(R.string.change)
        }
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.tiePassword.addTextChangedListener { binding.tilPassword.isErrorEnabled=false }
        binding.tieConfirmPassword.addTextChangedListener { binding.tilConfirmPassword.isErrorEnabled=false }
    }

    override fun observer() {
        binding.apply {
            tiePassword.addTextChangedListener { tilPassword.isErrorEnabled=false }
            tieConfirmPassword.addTextChangedListener { tilConfirmPassword.isErrorEnabled=false }
        }

        lifecycleScope.launch {
            viewModel.forgotPassword.observe(this@PasswordActivity, {
                buttonLoader(binding.clShadowButton,false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            toast(it.value?.message?:"")
                            startActivity(Intent(this@PasswordActivity, LoginWithPasswordActivity::class.java))
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
            R.id.ivButton ->{
                buttonLoader(binding = binding.clShadowButton,loader = true)
                if(checkValidation()) {
                if(data is SignupModel){
                    (data as SignupModel).password =  binding.tiePassword.text()
                    buttonLoader(binding.clShadowButton,false)
                    startActivity(Intent(this,ChooseAddressActivity::class.java).putExtra("data",data as SignupModel))

                 }else if(data is ForgotModel){
                    (data as ForgotModel).password =  binding.tiePassword.text()
                    viewModel.forgotPassword(data as ForgotModel)
                 }
                } else buttonLoader(binding = binding.clShadowButton,loader = false)
            }
        }
    }


    private fun checkValidation() : Boolean{
        var ret=true
        if(binding.tiePassword.text().isEmpty()){
            ret=false
            binding.tilPassword.isErrorEnabled=true
            binding.tilPassword.error=getString(R.string.enter_password)

        }

        else if(!isValidPassword(binding.tiePassword.text()) || binding.tiePassword.text()?.length?:0<PASSWORD_VALIDATION){
            ret=false
            binding.tilPassword.isErrorEnabled=true
            binding.tilPassword.error = getString(R.string.enter_password_character)
        }


        else if(binding.tieConfirmPassword.text().isEmpty()){
            ret=false
            binding.tilConfirmPassword.isErrorEnabled=true
            binding.tilConfirmPassword.error = getString(R.string.re_enter_password)
        }

        else if(!isValidPassword(binding.tieConfirmPassword.text()) || binding.tieConfirmPassword.text()?.length?:0< PASSWORD_VALIDATION){
            ret=false
            binding.tilConfirmPassword.isErrorEnabled=true
            binding.tilConfirmPassword.error = getString(R.string.enter_confirm_password_character)
        }

        else if(!binding.tiePassword.text().equals(binding.tieConfirmPassword.text())){
            ret=false
            binding.tilConfirmPassword.isErrorEnabled=true
            binding.tilConfirmPassword.error = getString(R.string.password_does_not_math)
        }

        return ret
    }

}