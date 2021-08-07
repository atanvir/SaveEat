package com.saveeat.ui.activity.auth.login

import android.content.Intent
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityLoginBinding
import com.saveeat.ui.activity.auth.forgot.ForgotPasswordActivity
import com.saveeat.ui.activity.auth.signup.SignUpActivity
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.utils.application.CommonUtils.authToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun inits() {
        authToolbar(this)
    }

    override fun initCtrl() {
        binding.tvSignup.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tvSignup ->{ startActivity(Intent(this,SignUpActivity::class.java)) }
            R.id.tvForgotPassword ->{ startActivity(Intent(this,ForgotPasswordActivity::class.java)) }
            R.id.ivButton ->{ startActivity(Intent(this,MainActivity::class.java))}
        }
    }
}