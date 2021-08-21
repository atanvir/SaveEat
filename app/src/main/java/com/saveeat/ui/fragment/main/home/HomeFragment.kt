package com.saveeat.ui.fragment.main.home

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHomeBinding
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.adapter.brand.BrandAdapter
import com.saveeat.ui.adapter.home.RestaurantHomeAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.ErrorUtil.snackView
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.KeyConstants.BOTH
import com.saveeat.utils.application.KeyConstants.VEG
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener, (Int,String?) -> Unit {
    private val viewModel : HomeViewModel by viewModels()
    private var requestModel : CommonHomeModel? =null
    private var popularRestraurantList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var allRestraurantList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var type : String?=null
    private var position : Int?=null

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        requestModel=CommonHomeModel(latitude=getPrefrenceStringValue(requireActivity(), latitude),
                                     longitude=getPrefrenceStringValue(requireActivity(), longitude),
                                     distance=getPrefrenceStringValue(requireActivity(), distance),
                                     foodType = VEG,limit = 10, token = getPrefrenceStringValue(requireActivity(), jwtToken))
        binding.clShimmer.shimmerContainer.startShimmer()


        viewModel.moreSaveProductList(requestModel)
    }


    override fun initCtrl() {
        binding.cpType.setOnClickListener(this)
        binding.cpType.setOnCloseIconClickListener {
            binding.cpType.isCloseIconVisible=false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.white)
                binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.black)
            }
            binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.black))
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility=View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility=View.GONE
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.moreSaveProductList.observe(this@HomeFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            binding.rvSaveProducts.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvSaveProducts.adapter=SavedRestaurantAdapter(requireActivity(),it.value?.data?.toMutableList()!!)
                            viewModel.popularRestaurantList(requestModel)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer();  snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.addToFavourite.observe(this@HomeFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            when(type){
                                "Popular" ->{
                                    popularRestraurantList?.get(position?:0)?.isFav=!(popularRestraurantList?.get(position?:0)?.isFav!!)
                                    binding.rvPopularProducts.adapter?.notifyItemChanged(position?:0)
                                }
                                "All" ->{
                                    allRestraurantList?.get(position?:0)?.isFav=!(allRestraurantList?.get(position?:0)?.isFav!!)
                                    binding.rvAllRestro.adapter?.notifyItemChanged(position?:0)
                                }
                            }
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })



            viewModel.popularRestaurantList.observe(this@HomeFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            popularRestraurantList=it.value?.data?.toMutableList()!!
                            binding.rvPopularProducts.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvPopularProducts.adapter= RestaurantHomeAdapter(requireActivity(),popularRestraurantList,"Popular",this@HomeFragment)
                            viewModel.newRestaurantList(requestModel)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer(); snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.newRestaurantList.observe(this@HomeFragment,{

                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            binding.rvBrands.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvBrands.adapter= BrandAdapter(requireActivity(),it.value?.data?.toMutableList()!!)
                            viewModel.restaurantForHomeList(requestModel)


                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer();  snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.restaurantForHomeList.observe(this@HomeFragment,{
                stopShimmer()
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            allRestraurantList=it.value?.data?.toMutableList()!!

                            binding.rvAllRestro.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvAllRestro.adapter= RestaurantHomeAdapter(requireActivity(),allRestraurantList,"All",this@HomeFragment)

                            binding.clMain.visibility=View.VISIBLE
                            if(binding.rvSaveProducts.adapter?.itemCount?:0>0) binding.clSaveProducts.visibility=View.VISIBLE
                            if(binding.rvPopularProducts.adapter?.itemCount?:0>0) binding.clPopularRestro.visibility=View.VISIBLE
                            if(binding.rvBrands.adapter?.itemCount?:0>0) binding.clBrand.visibility=View.VISIBLE
                            if(binding.rvAllRestro.adapter?.itemCount?:0>0) binding.clAllRestro.visibility=View.VISIBLE

                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { snackView(binding.root,it.value?.message?:"") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

        }
    }

    private fun stopShimmer() {
        binding.clShimmer.shimmerContainer.stopShimmer()
        binding.clShimmer.shimmerContainer.visibility=View.GONE
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cpType ->{
                binding.cpType.isCloseIconVisible=true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.app_theme)
                    binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.white)
                }
                binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            }
        }
    }

    override fun invoke(position: Int, type: String?) {
        this.type=type
        this.position=position
        when(type){
            "Popular" ->{ viewModel?.addToFavourite(popularRestraurantList?.get(position)?._id, getPrefrenceStringValue(requireActivity(), jwtToken)) }
            "All" ->{ viewModel?.addToFavourite(allRestraurantList?.get(position)?._id, getPrefrenceStringValue(requireActivity(), jwtToken)) }
        }
    }


}