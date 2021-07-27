package com.saveeat.ui.fragment.main.home

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
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHomeBinding
import com.saveeat.ui.adapter.menu.CategoryAdapter
import com.saveeat.utils.application.StaticDataHelper.homeData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        binding.rvHome.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvHome.adapter=CategoryAdapter(requireActivity(),homeData())
    }

    override fun initCtrl() {
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility=View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility=View.GONE
    }

    override fun observer() {
    }

}