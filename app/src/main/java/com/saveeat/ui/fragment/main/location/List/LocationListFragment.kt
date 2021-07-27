package com.saveeat.ui.fragment.main.location.List

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentLocationListBinding
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.utils.application.StaticDataHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment : BaseFragment<FragmentLocationListBinding>() {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationListBinding = FragmentLocationListBinding.inflate(inflater,container,false)

    override fun init() {
        binding.rvListRestro.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvListRestro.adapter= RestaurantByLocationAdapter(requireActivity(),StaticDataHelper.getLocationByRestraurant())
    }

    override fun initCtrl() {
    }

    override fun observer() {

    }

}