package com.saveeat.ui.activity.auth.password

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityPasswordBinding
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.extn.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordActivity : BaseActivity<ActivityPasswordBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityPasswordBinding = ActivityPasswordBinding.inflate(layoutInflater)

    override fun inits() {
       authToolbar(this)

       if(intent.getStringExtra("type").equals("forgot")){
            binding.tvTitleLabel.text="Reset Password"
            binding.tvDescLabel.text="Enter a new Password"
            binding.clShadowButton.tvButtonLabel.text="Change"
        }else{
           binding.clShadowButton.tvButtonLabel.text="Save"
        }
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{
                if(intent.getStringExtra("type").equals("signup")){
                    startActivity(Intent(this,ChooseAddressActivity::class.java))
                }else{
                    toast("Your password has been reset")
                    finish()
                    startActivity(Intent(this,LoginActivity::class.java))
                }
            }
        }
    }

}