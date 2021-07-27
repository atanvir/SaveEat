package com.saveeat.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.activity.walkthrough.WalkThroughActivity
import com.saveeat.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, WalkThroughActivity::class.java))
    }
}