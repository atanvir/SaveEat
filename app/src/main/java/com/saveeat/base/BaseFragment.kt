package com.saveeat.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.saveeat.R
import com.saveeat.databinding.FragmentGlobalSearchBinding
import com.saveeat.databinding.FragmentSearchBinding

abstract class BaseFragment<B: ViewBinding> : Fragment() {
    protected lateinit var callback: OnBackPressedCallback
    protected lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=getFragmentBinding(inflater, container)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initCtrl()
        observer()

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    if(binding is FragmentGlobalSearchBinding) Navigation.findNavController(view).navigate(R.id.searchFragment)
                    else Navigation.findNavController(view).popBackStack()
                    this.remove()
                }catch (e: Exception){
                    requireActivity().finish()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun init()
    abstract fun initCtrl()
    abstract fun observer()
}