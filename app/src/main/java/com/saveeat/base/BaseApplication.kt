package com.saveeat.base

import android.app.Application
import com.google.android.gms.maps.MapView
import com.saveeat.utils.application.CommonUtils.generateFCMToken
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread {
            try {
                val mv = MapView(applicationContext)
                mv.onCreate(null)
                mv.onPause()
                mv.onDestroy()
            } catch (ignored: Exception) {
            }finally {
                generateFCMToken(this)
            }
        }.start()
    }
}