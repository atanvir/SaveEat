package com.saveeat.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saveeat.ui.fragment.main.location.List.LocationListFragment
import com.saveeat.ui.fragment.main.location.Map.LocationMapFragment

class LocationTabPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment = if(position==0) LocationListFragment() else LocationMapFragment()
}