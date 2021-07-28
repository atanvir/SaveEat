package com.saveeat.ui.fragment.main.location


import android.R.attr.button
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentTabLocationBinding
import com.saveeat.ui.adapter.viewpager.LocationTabPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationTabFragment : BaseFragment<FragmentTabLocationBinding>(), TabLayout.OnTabSelectedListener {

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTabLocationBinding = FragmentTabLocationBinding.inflate(inflater,container,false)

    override fun init() {
        binding.viewPager.adapter= LocationTabPagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if(position==0) {
                tab.text ="Map"

            }
            else {
                tab.text ="List"
                binding.viewPager.isUserInputEnabled=false
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.GONE
    }


    override fun initCtrl() {
        binding.tabLayout.addOnTabSelectedListener(this)
    }

    override fun observer() {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position==0){
            binding.supportingView.visibility=View.GONE
            binding.viewPager.isUserInputEnabled=false
        }else{
            binding.supportingView.visibility=View.VISIBLE
            binding.viewPager.isUserInputEnabled=false
        }
        binding.viewPager.currentItem = tab?.position ?:0;
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

}