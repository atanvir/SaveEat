package com.saveeat.ui.fragment.order.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentUpcomingBinding
import com.saveeat.ui.adapter.order.HistoryOrderAdapter
import com.saveeat.ui.adapter.order.UpcomingOrderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : BaseFragment<FragmentUpcomingBinding>() {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentUpcomingBinding = FragmentUpcomingBinding.inflate(inflater,container,false)

    override fun init() {
        binding.rvUpcomingOrders.layoutManager= LinearLayoutManager(requireActivity())
        binding.rvUpcomingOrders.adapter= UpcomingOrderAdapter(requireActivity())
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

}