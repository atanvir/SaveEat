package com.saveeat.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<B: ViewBinding>: AppCompatActivity(){
    protected lateinit var binding : B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= getActivityBinding()
        adjustFontScale(resources.configuration)
        setContentView(binding.root)
        inits()
        initCtrl()
        observer()
    }

    abstract fun getActivityBinding() : B
    abstract fun inits()
    abstract fun initCtrl()
    abstract fun observer()

    open fun adjustFontScale(configuration: Configuration) {
        configuration.fontScale = 1f
        val metrics = resources.displayMetrics
        val wm = getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

}