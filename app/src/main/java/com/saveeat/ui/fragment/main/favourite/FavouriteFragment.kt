package com.saveeat.ui.fragment.main.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentFavouriteBinding
import com.saveeat.model.request.main.favourite.FavouriteModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.home.RestaurantHomeAdapter
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(), (Int, String) -> Unit {
    private val viewModel : FavouriteViewModel by viewModels()
    private var list : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var type : String?=null
    private var position : Int?=null

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
        binding.clShimmer.shimmerContainer.startShimmer()
        viewModel.getFavoriteRestaurants(FavouriteModel(latitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), latitude),
                                                        longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), longitude),
                                                        token= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), jwtToken)))
    }
    override fun initCtrl() {
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.getFavoriteRestaurants.observe(this@FavouriteFragment,{
                stopShimmer()
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list=it.value?.data?.toMutableList()!!
                            binding.rvFavourites.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvFavourites.adapter= RestaurantHomeAdapter(requireActivity(),list,"Favourite",this@FavouriteFragment)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {  ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }

            })


            viewModel.addToFavourite.observe(this@FavouriteFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list?.removeAt(position?:0)
                            binding.rvFavourites.adapter?.notifyItemRemoved(position?:0)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    private fun stopShimmer(){
        binding.clShimmer.shimmerContainer.stopShimmer()
        binding.clShimmer.shimmerContainer.visibility=View.GONE
    }

    override fun invoke(position: Int, type: String) {
        this.type=type
        this.position=position
        viewModel?.addToFavourite(list?.get(position)?.restroData?._id, PrefrencesHelper.getPrefrenceStringValue(requireActivity(), jwtToken))
    }

}