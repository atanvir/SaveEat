package com.saveeat.ui.adapter.walkthrough

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.saveeat.databinding.AdapterWalkThroughBinding
import com.saveeat.model.request.walkthrough.WalkThroughModel

class WalkThroughAdapter(val context: Context, val list: MutableList<WalkThroughModel?>?) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding=AdapterWalkThroughBinding.inflate(LayoutInflater.from(context),container,false)
        binding.data=list?.get(position)
        val viewPager: ViewPager = container as ViewPager
        viewPager.addView(binding.root,0)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =view==`object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View)
    override fun getCount(): Int =list?.size?:0
}
