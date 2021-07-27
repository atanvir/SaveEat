package com.saveeat.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saveeat.ui.fragment.main.location.List.LocationListFragment
import com.saveeat.ui.fragment.main.location.Map.LocationMapFragment
import com.saveeat.ui.fragment.order.history.HistoryFragment
import com.saveeat.ui.fragment.order.upcoming.UpcomingFragment

class OrderTabPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int =2
    override fun createFragment(position: Int): Fragment = if(position==0) UpcomingFragment() else HistoryFragment()
}