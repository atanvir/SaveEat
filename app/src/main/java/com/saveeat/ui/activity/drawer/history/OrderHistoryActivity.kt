package com.saveeat.ui.activity.drawer.history

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityOrderHistoryBinding
import com.saveeat.ui.adapter.viewpager.OrderTabPagerAdapter
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryActivity : BaseActivity<ActivityOrderHistoryBinding>(), TabLayout.OnTabSelectedListener {

    override fun getActivityBinding(): ActivityOrderHistoryBinding = ActivityOrderHistoryBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.viewPager.adapter= OrderTabPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if(position==0) tab.text ="Upcoming" else tab.text ="History"
        }.attach()
    }

    override fun initCtrl() {
        binding.tabLayout.addOnTabSelectedListener(this)
    }

    override fun observer() {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        binding.viewPager.currentItem = tab?.position ?:0
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}