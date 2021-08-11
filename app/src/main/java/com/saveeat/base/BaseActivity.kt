package com.saveeat.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewbinding.ViewBinding
import com.saveeat.repository.cache.PreferenceKeyConstants


abstract class BaseActivity<B: ViewBinding>: AppCompatActivity(){
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceKeyConstants.CACHE_NAME)

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