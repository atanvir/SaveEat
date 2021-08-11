package com.saveeat.ui.activity.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.asLiveData
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivitySplashBinding
import com.saveeat.repository.cache.DataStoreHelper
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.ui.activity.walkthrough.WalkThroughActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getActivityBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun inits() {
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            startActivity(Intent(this, WalkThroughActivity::class.java))
        },2000)

       val flow: Flow<Any>?= dataStore.data.map {
           it[PreferenceKeyConstants.name]?:""
           it[PreferenceKeyConstants._id]?:""
       }
        flow?.asLiveData()?.observe(this){
           Log.e("TAG", "------->"+it.toString())
        }


        CoroutineScope(Dispatchers.Main).launch {
            coroutineScope {
                DataStoreHelper(dataStore).storeUserData(id=101,_name="Tanvir Ahmed")
            }
        }





    }

    override fun initCtrl() {
    }

    override fun observer() {
    }
}