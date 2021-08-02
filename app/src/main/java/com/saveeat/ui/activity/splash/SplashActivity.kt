package com.saveeat.ui.activity.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivitySplashBinding
import com.saveeat.ui.activity.walkthrough.WalkThroughActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getActivityBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun inits() {
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            startActivity(Intent(this, WalkThroughActivity::class.java))
        },2000)
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }
}