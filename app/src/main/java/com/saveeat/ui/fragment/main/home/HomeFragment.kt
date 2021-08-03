package com.saveeat.ui.fragment.main.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHomeBinding
import com.saveeat.ui.adapter.menu.CategoryAdapter
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.StaticDataHelper.homeData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        binding.rvHome.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvHome.adapter=CategoryAdapter(requireActivity(),homeData())
    }

    override fun initCtrl() {
        binding.cpType.setOnClickListener(this)
        binding.cpType.setOnCloseIconClickListener {
            binding.cpType.isCloseIconVisible=false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.white)
                binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.black)
            }
            binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.black))

        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility=View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility=View.GONE
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cpType ->{
                binding.cpType.isCloseIconVisible=true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.app_theme)
                    binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.white)
                }
                binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            }


        }
    }

}