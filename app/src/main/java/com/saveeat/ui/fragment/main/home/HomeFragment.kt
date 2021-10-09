package com.saveeat.ui.fragment.main.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHomeBinding
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.distance
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.activity.filter.FilterActivity
import com.saveeat.ui.activity.location.ChooseAddressActivity
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

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener, (Int,String?) -> Unit {
    private val viewModel : HomeViewModel by viewModels()
    private var requestModel : CommonHomeModel? =null
    private var popularRestraurantList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var allRestraurantList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var type : String?=null
    private var position : Int?=null
    private var distanceBroadcast: BroadcastReceiver?=null

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater,container,false)

    override fun init() {
        binding.clShadowButton.tvButtonLabel.text="Search for new location"
        requestModel=CommonHomeModel(latitude=getPrefrenceStringValue(requireActivity(), latitude),
                                     longitude=getPrefrenceStringValue(requireActivity(), longitude),
                                     distance=getPrefrenceStringValue(requireActivity(), distance),
                                     foodType = BOTH,
                                     limit = 10,
                                     token = getPrefrenceStringValue(requireActivity(), jwtToken),
                                     userId=getPrefrenceStringValue(requireActivity(), _id))
        startShimmerAnimation()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(distanceBroadcast, IntentFilter("com.saveeat"))
    }

    override fun onDestroy() {
        super.onDestroy()
        try{ if(distanceBroadcast!=null) requireActivity().unregisterReceiver(distanceBroadcast) }catch (e: Exception){ }
    }


    override fun initCtrl() {
        binding.cpType.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.ivFilter.setOnClickListener(this)
        binding.rvSaveProducts.addOnScrollListener(saveRestroRecycleviewListner)
        binding.svLocation.addTextChangedListener {
            if(binding.rvSaveProducts.adapter!=null) (binding.rvSaveProducts.adapter as SavedRestaurantAdapter).filter.filter(it)
            if(binding.rvPopularProducts.adapter !=null) (binding.rvPopularProducts.adapter as RestaurantHomeAdapter).filter.filter(it)
            if(binding.rvBrands.adapter !=null) (binding.rvBrands.adapter as BrandAdapter).filter.filter(it)
            if(binding.rvAllRestro.adapter !=null) (binding.rvAllRestro.adapter as RestaurantHomeAdapter).filter.filter(it)

            Handler(Looper.getMainLooper()).postDelayed(Runnable {

                if(binding.rvSaveProducts.adapter!=null){
                if(binding.rvSaveProducts?.adapter?.itemCount?:0>0) binding.clSaveProducts.visibility=View.VISIBLE
                else binding.clSaveProducts.visibility=View.GONE
                }



                if(binding.rvPopularProducts.adapter !=null) {
                    if (binding.rvPopularProducts?.adapter?.itemCount ?: 0 > 0) binding.clPopularRestro.visibility = View.VISIBLE
                    else binding.clPopularRestro.visibility = View.GONE
                }

                if(binding.rvBrands.adapter !=null) {
                    if (binding.rvBrands?.adapter?.itemCount ?: 0 > 0) binding.clBrand.visibility = View.VISIBLE
                    else binding.clBrand.visibility = View.GONE
                }

                if(binding.rvAllRestro.adapter !=null) {
                    if(binding.rvAllRestro?.adapter?.itemCount?:0>0) binding.clAllRestro.visibility=View.VISIBLE
                    else binding.clAllRestro.visibility=View.GONE
                }


            },1000)
        }

        binding.cpType.setOnCloseIconClickListener {
            requestModel?.foodType= BOTH
            startShimmerAnimation()
            binding.cpType.isCloseIconVisible=false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.white)
                binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.black)
            }
            binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.black))
        }
    }

    val saveRestroRecycleviewListner = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(binding.rvSaveProducts.adapter!=null){
                val layoutParams= binding.rvSaveProducts.layoutParams as ViewGroup.MarginLayoutParams
                if((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()==0 && (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()==0) layoutParams.leftMargin=19
                else layoutParams.leftMargin=0
                binding.rvSaveProducts.layoutParams=layoutParams
                binding.rvSaveProducts.requestLayout()
            }
        }

    }


    private fun startShimmerAnimation(){
        binding.svLocation.visibility=View.VISIBLE
        binding.cpType.visibility=View.VISIBLE
        binding.ivFilter.visibility=View.VISIBLE
        binding.clMain.visibility=View.GONE
        binding.clNoData.visibility=View.GONE
        binding.clShimmer.shimmerContainer.startShimmer()
        binding.clShimmer.shimmerContainer.visibility=View.VISIBLE
        viewModel.moreSaveProductList(requestModel)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility=View.VISIBLE
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility=View.GONE
    }

    override fun observer() {
        lifecycleScope.launch {


            distanceBroadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    requestModel?.distance=intent.getStringExtra("distance")
                    startShimmerAnimation()
                }
            }

            viewModel.moreSaveProductList.observe(this@HomeFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            binding.rvSaveProducts.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvSaveProducts.adapter=SavedRestaurantAdapter(requireActivity(),it.value?.data?.toMutableList()!!,it.value?.data?.toMutableList()!!)
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
                            binding.rvPopularProducts.adapter= RestaurantHomeAdapter(requireActivity(),popularRestraurantList,"Popular",this@HomeFragment,it.value?.data?.toMutableList()!!)
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
                            binding.rvBrands.adapter= BrandAdapter(requireActivity(),it.value?.data?.toMutableList()!!,it.value?.data?.toMutableList()!!)
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
                            binding.rvAllRestro.adapter= RestaurantHomeAdapter(requireActivity(),allRestraurantList,"All",this@HomeFragment,it.value?.data?.toMutableList()!!)

                            binding.clMain.visibility=View.VISIBLE
                            if(binding.rvSaveProducts.adapter?.itemCount?:0>0) binding.clSaveProducts.visibility=View.VISIBLE else binding.clSaveProducts.visibility=View.GONE
                            if(binding.rvPopularProducts.adapter?.itemCount?:0>0) binding.clPopularRestro.visibility=View.VISIBLE else binding.clPopularRestro.visibility=View.GONE
                            if(binding.rvBrands.adapter?.itemCount?:0>0) binding.clBrand.visibility=View.VISIBLE else binding.clBrand.visibility=View.GONE
                            if(binding.rvAllRestro.adapter?.itemCount?:0>0) binding.clAllRestro.visibility=View.VISIBLE else binding.clAllRestro.visibility=View.GONE
                            if(binding.clSaveProducts.visibility==View.GONE && binding.clPopularRestro.visibility==View.GONE  && binding.clBrand.visibility==View.GONE  && binding.clAllRestro.visibility==View.GONE)
                            {
                                binding.svLocation.visibility=View.GONE
                                binding.cpType.visibility=View.GONE
                                binding.ivFilter.visibility=View.GONE
                                binding.clNoData.visibility=View.VISIBLE
                            }
                            else {
                                binding.svLocation.visibility=View.VISIBLE
                                binding.cpType.visibility=View.VISIBLE
                                binding.ivFilter.visibility=View.VISIBLE
                                binding.clNoData.visibility=View.GONE
                            }


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

            R.id.ivButton -> {
                laucher.launch(Intent(requireActivity(), ChooseAddressActivity::class.java).putExtra("data", LocationModel(address = getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.address),latitude=getPrefrenceStringValue(requireActivity(),latitude).toDouble(),longitude=getPrefrenceStringValue(requireActivity(), longitude).toDouble(),distance=getPrefrenceStringValue(requireActivity(),distance))))
            }
            R.id.cpType ->{
                requestModel?.foodType= VEG
                startShimmerAnimation()
                binding.cpType.isCloseIconVisible=true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.cpType.chipBackgroundColor=requireActivity().getColorStateList(R.color.app_theme)
                    binding.cpType.closeIconTint=requireActivity().getColorStateList(R.color.white)
                }
                binding.cpType.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            }

            R.id.ivFilter ->{ startActivity(Intent(requireActivity(),FilterActivity::class.java)) }
        }
    }

    override fun invoke(position: Int, type: String?) {

    }

    private var laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== AppCompatActivity.RESULT_OK) {
            val location=it.data?.getParcelableExtra<LocationModel>("data")
            PrefrencesHelper.updateLocation(requireActivity(), location)
            startShimmerAnimation()

        }
    }


}