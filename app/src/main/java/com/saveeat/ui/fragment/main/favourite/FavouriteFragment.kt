package com.saveeat.ui.fragment.main.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentFavouriteBinding
import com.saveeat.ui.adapter.home.HomeCategoryAdapter
import com.saveeat.utils.application.StaticDataHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavouriteBinding = FragmentFavouriteBinding.inflate(inflater,container,false)

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.GONE
        activity?.findViewById<TextView>(R.id.tvTitle)?.text=getString(R.string.favourites)
        activity?.findViewById<TextView>(R.id.tvTitle)?.compoundDrawablePadding=16
        activity?.findViewById<TextView>(R.id.tvTitle)?.setCompoundDrawablesWithIntrinsicBounds(activity?.getDrawable(R.drawable.fi_sr_heart25),null,null,null)
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.VISIBLE
    }

    override fun init() {
        binding.rvFavourites.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvFavourites.adapter= HomeCategoryAdapter(requireActivity(),StaticDataHelper.getFavData())
    }
    override fun initCtrl() {
    }

    override fun observer() {
    }
}