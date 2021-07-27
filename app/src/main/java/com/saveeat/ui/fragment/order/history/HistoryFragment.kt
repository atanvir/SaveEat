package com.saveeat.ui.fragment.order.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHistoryBinding
import com.saveeat.ui.adapter.order.HistoryOrderAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.utils.application.StaticDataHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater,container,false)

    override fun init() {
        binding.rvHistoryOrders.layoutManager= LinearLayoutManager(requireActivity())
        binding.rvHistoryOrders.adapter= HistoryOrderAdapter(requireActivity())
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

}