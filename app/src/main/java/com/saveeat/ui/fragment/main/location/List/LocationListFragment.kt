package com.saveeat.ui.fragment.main.location.List

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentLocationListBinding
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.ui.fragment.main.location.LocationViewModel
import com.saveeat.utils.application.*
import com.saveeat.utils.application.KeyConstants.VEG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationListFragment : BaseFragment<FragmentLocationListBinding>(), (Int) -> Unit {
    private val viewModel : LocationViewModel by viewModels()
    private var list : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var position : Int?=null


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationListBinding = FragmentLocationListBinding.inflate(inflater,container,false)

    override fun init() {
        binding.clShimmer.shimmerContainer.startShimmer()
        viewModel.restaurantList(CommonHomeModel(latitude= getPrefrenceStringValue(requireActivity(), latitude),
                                                longitude=getPrefrenceStringValue(requireActivity(), longitude),
                                                distance= getPrefrenceStringValue(requireActivity(), distance),
                                                foodType = VEG,limit = 10,
                                                token = getPrefrenceStringValue(requireActivity(), jwtToken),
                                                userId=getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))
    }

    override fun initCtrl() {
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.restaurantList.observe(this@LocationListFragment,{
                binding.clShimmer.shimmerContainer.stopShimmer()
                binding.clShimmer.shimmerContainer.visibility= View.GONE
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list=it.value?.data
                            binding.rvListRestro.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvListRestro.adapter= RestaurantByLocationAdapter(requireActivity(),list,this@LocationListFragment)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })



            viewModel.addToFavourite.observe(this@LocationListFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list?.get(position?:0)?.isFav=!(list?.get(position?:0)?.isFav!!)
                            binding.rvListRestro?.adapter?.notifyItemChanged(position?:0)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    override fun invoke(position: Int) {
        this.position=position
        viewModel?.addToFavourite(list?.get(position?:0)?._id,getPrefrenceStringValue(requireActivity(), jwtToken))
    }

}