package com.saveeat.ui.fragment.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentGlobalSearchBinding
import com.saveeat.model.request.main.search.GlobalSearchModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.menu.MenuItemProductModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.menu.MenuProductAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.ui.fragment.main.home.HomeViewModel
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.hideKeyboard
import com.saveeat.utils.extn.onAfterChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GlobalSearchFragment : BaseFragment<FragmentGlobalSearchBinding>(), (Int) -> Unit, TabLayout.OnTabSelectedListener {
    var globalSearch : GlobalSearchModel?=null
    private val homeViewModel : HomeViewModel by viewModels()
    private var position : Int?=null

    private val viewModel : SearchViewModel by viewModels()

    var restaurantList:MutableList<RestaurantResponseBean?>?=ArrayList()
    var dishesList: MutableList<RestaurantResponseBean?>?=ArrayList()

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGlobalSearchBinding = FragmentGlobalSearchBinding.inflate(inflater,container,false)

    override fun init() {
        binding.root.hideKeyboard(requireActivity())
        globalSearch=arguments?.getParcelable("data")
        binding.svLocation.setText(globalSearch?.search?:"")
        viewModel.globalSearch(globalSearch)
    }

    override fun initCtrl() {
        binding.svLocation.onAfterChanged {
            globalSearch?.search=it
            if(it.isNotEmpty()){
            binding.progresBar.visibility=View.VISIBLE
            viewModel.globalSearch(globalSearch)
            }else{
                restaurantList?.clear()
                dishesList?.clear()
                binding.rvData?.adapter?.notifyDataSetChanged()
            }
        }

        binding.tabLayout.addOnTabSelectedListener(this)
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.globalSearch.observe(this@GlobalSearchFragment,{
                binding.progresBar.visibility= View.GONE
                when (it) {
                is Resource.Success -> {
                if(KeyConstants.SUCCESS==it.value?.status?:0) {
                    restaurantList=it?.value?.data?.restaurantSearch
                    dishesList=it?.value?.data?.prouctNameSearch

                    if(binding.tabLayout.selectedTabPosition==0){
                        binding.rvData.layoutManager= LinearLayoutManager(requireActivity())
                        binding.rvData.adapter= RestaurantByLocationAdapter(requireActivity(),restaurantList,null,this@GlobalSearchFragment,"")
                    }else{

                        for(i in dishesList?.indices!!){
                            if(dishesList?.get(i)?.sellingStatus == true) dishesList?.get(i)?.type="Selling"
                            else  dishesList?.get(i)?.type="Fix"
                        }
                        binding.rvData.layoutManager=GridLayoutManager(requireActivity(),2)
                        binding.rvData.adapter= MenuProductAdapter(requireActivity(),dishesList,null,"")
                    }
                }
                else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") } }
                is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) } }
            })

            homeViewModel.addToFavourite.observe(this@GlobalSearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            restaurantList?.get(position?:0)?.isFav=!(restaurantList?.get(position?:0)?.isFav!!)
                            binding.rvData.adapter?.notifyItemChanged(position?:0)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                        }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    override fun invoke(p1: Int) {
        this.position=p1
        homeViewModel?.addToFavourite(restaurantList?.get(position?:0)?._id, PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken))

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position==0) {
            binding.rvData.layoutManager= LinearLayoutManager(requireActivity())
            binding.rvData.adapter= RestaurantByLocationAdapter(requireActivity(),restaurantList,null,this@GlobalSearchFragment,"")
        }
        else {
            for(i in dishesList?.indices!!){
                if(dishesList?.get(i)?.sellingStatus==true) dishesList?.get(i)?.type="Selling"
                else dishesList?.get(i)?.type="Fix"

            }
            binding.rvData.layoutManager=GridLayoutManager(requireActivity(),2)
            binding.rvData.adapter= MenuProductAdapter(requireActivity(),dishesList,dishesList,"")
        }

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

}