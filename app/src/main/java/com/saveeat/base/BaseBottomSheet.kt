package com.saveeat.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saveeat.repository.cache.DataStore


abstract class BaseBottomSheet<B: ViewBinding> : BottomSheetDialogFragment()
{
    protected lateinit var binding : B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=getBottomSheetBinding(inflater, container)
        return binding.root
    }

    override fun getTheme(): Int {
        return com.saveeat.R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initCtrl()
        observer()
    }

    abstract fun getBottomSheetBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun init()
    abstract fun initCtrl()
    abstract fun observer()
}