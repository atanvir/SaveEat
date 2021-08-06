package com.saveeat.ui.activity.auth.password

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityPasswordBinding
import com.saveeat.ui.activity.auth.login.LoginActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.extn.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordActivity : BaseActivity<ActivityPasswordBinding>(), View.OnClickListener {
    override fun getActivityBinding(): ActivityPasswordBinding = ActivityPasswordBinding.inflate(layoutInflater)

    override fun inits() {
       authToolbar(this)
       if(intent.getStringExtra("type").equals(KeyConstants.FORGOT)){
            binding.tvTitleLabel.text=getString(R.string.reset_password)
            binding.tvDescLabel.text=getString(R.string.enter_new_password)
            binding.clShadowButton.tvButtonLabel.text=getString(R.string.change)
        }else{
           binding.clShadowButton.tvButtonLabel.text=getString(R.string.save)
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
                if(intent.getStringExtra("type").equals(KeyConstants.SIGNUP)){
                    startActivity(Intent(this,ChooseAddressActivity::class.java))
                }else{
                    toast(getString(R.string.password_has_been_reset))
                    finish()
                    startActivity(Intent(this,LoginActivity::class.java))
                }
            }
        }
    }

}