package com.saveeat.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B: ViewBinding>: AppCompatActivity(){
    protected lateinit var binding : B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= getActivityBinding()
        setContentView(binding.root)
        inits()
        initCtrl()
        observer()
    }

    abstract fun getActivityBinding() : B
    abstract fun inits()
    abstract fun initCtrl()
    abstract fun observer()
}