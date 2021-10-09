package com.saveeat.ui.activity.splash

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivitySplashBinding
import com.saveeat.repository.cache.PreferenceKeyConstants.login
import com.saveeat.repository.cache.PreferenceKeyConstants.walkthrow
import com.saveeat.ui.activity.auth.login.otp.LoginWithOTPActivity
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.walkthrough.WalkThroughActivity
import com.saveeat.utils.application.KeyConstants.PREF_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getActivityBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun inits() {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).apply {
            when {
                getBoolean(login,false) -> splashIntent(MainActivity::class.java)
                getBoolean(walkthrow,false) -> splashIntent(LoginWithOTPActivity::class.java)
                else -> splashIntent(WalkThroughActivity::class.java)
            }
        }
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

    private fun  <A : Activity> splashIntent(activity: Class<A>) {
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity,activity))
        }, 2000)
    }

}