package com.saveeat.ui.fragment.main.location


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
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
        binding.viewPager.isUserInputEnabled=false
        binding.viewPager.adapter= LocationTabPagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if(position==0)  tab.text =getString(R.string.map)
            else tab.text =getString(R.string.list)
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.GONE
    }


    override fun initCtrl() {
        binding.tabLayout.addOnTabSelectedListener(this)
        binding.svLocation.doAfterTextChanged {
            val intent = Intent("com.saveeat")
            intent.putExtra("search",it?.toString()?:"")
            intent.putExtra("update",true)
            requireActivity().sendBroadcast(intent)
        }
    }

    override fun observer() {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position==0) binding.supportingView.visibility=View.GONE
        else binding.supportingView.visibility=View.VISIBLE

        binding.viewPager.currentItem = tab?.position ?:0
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

}