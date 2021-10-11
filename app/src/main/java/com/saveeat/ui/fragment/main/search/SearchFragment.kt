package com.saveeat.ui.fragment.main.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentSearchBinding
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.main.search.GlobalSearchModel
import com.saveeat.model.request.main.search.SaveRecentSearch
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.brand.BrandAdapter
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.ui.adapter.search.CuisinesAdapter
import com.saveeat.ui.adapter.search.SearchSuggestionAdapter
import com.saveeat.ui.adapter.search.RecentSearchAdapter
import com.saveeat.ui.fragment.main.home.HomeViewModel
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.onAfterChanged
import com.saveeat.utils.extn.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), (Int) -> Unit,View.OnClickListener {
    private val homeViewModel : HomeViewModel by viewModels()
    private val viewModel : SearchViewModel by viewModels()
    private var requestModel : CommonHomeModel? =null
    private var globalSearchModel : GlobalSearchModel? =null

    private var popularList : MutableList<RestaurantResponseBean?>? = ArrayList()
    private var position : Int?=null


    override fun onResume() {
        super.onResume()

        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.GONE
        activity?.findViewById<TextView>(R.id.tvTitle)?.text=getString(R.string.search)
        activity?.findViewById<TextView>(R.id.tvTitle)?.compoundDrawablePadding=16
        activity?.findViewById<TextView>(R.id.tvTitle)?.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.VISIBLE

    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater,container,false)

    override fun init() {
        requestModel= CommonHomeModel(latitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude),
                                      longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude),
                                      distance= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.distance),
                                      foodType = KeyConstants.BOTH,
                                      limit = 3,
                                      token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                      userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id))

        globalSearchModel= GlobalSearchModel(latitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude),
                                             longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude),
                                             distance= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.distance),
                                             foodType = KeyConstants.BOTH,
                                             limit = 10000,
                                             token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                             userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id),
                                             search = binding.svName.text.toString())
        viewModel.getRecentSearch(requestModel)
        viewModel.restaurantList(requestModel)
    }

    override fun initCtrl() {
        binding.svName.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    globalSearchFragment()
                    return true
                }
                return false
            }

        })
        binding.svName.onAfterChanged {
            if(it?.length?:0>1){
               binding.progressBar.visibility=View.VISIBLE
               binding.clBrand.visibility=View.GONE
               binding.clRecentSearch.visibility=View.GONE
               binding.clPopularRestro.visibility=View.GONE
               binding.clPopularCuisines.visibility=View.GONE
               binding.rvGlobalSearch.visibility=View.VISIBLE
               globalSearchModel?.search=it?.toString()?:""
               viewModel.searchSuggestions(globalSearchModel)
            }else{
                if(binding?.rvBrands?.adapter?.itemCount?:0>0) binding.clBrand.visibility=View.VISIBLE
                if(binding.rvRecentSearch.adapter?.itemCount?:0>0) binding.clRecentSearch.visibility=View.VISIBLE
                if(binding.rvPopularProducts?.adapter?.itemCount?:0>0) binding.clPopularRestro.visibility=View.VISIBLE
                if(binding?.rvCuisines?.adapter?.itemCount?:0>0) binding.clPopularCuisines.visibility=View.VISIBLE
                binding.rvGlobalSearch.visibility=View.GONE
                viewModel.getRecentSearch(requestModel)
            }
        }
        binding.tvRecentShowMore.setOnClickListener(this)


    }

    override fun observer() {
        lifecycleScope.launch {

            viewModel.restaurantList.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            popularList=it.value?.data
                            binding.rvPopularProducts.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvPopularProducts.adapter=RestaurantByLocationAdapter(requireActivity(),popularList,null,this@SearchFragment,"Search")
                            viewModel.popularCuisineProducts(requestModel)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { binding.progressBar.visibility=View.GONE; ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.visibility=View.GONE
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.searchSuggestions.observe(this@SearchFragment,{
                binding.progressBar.visibility=View.GONE
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.rvGlobalSearch.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvGlobalSearch.adapter=SearchSuggestionAdapter(requireActivity(),it.value?.data?.mergeData) { globalSearchFragment() }
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })



            viewModel.getRecentSearch.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.rvRecentSearch.layoutManager=LinearLayoutManager(requireActivity())
                            binding.rvRecentSearch.adapter=RecentSearchAdapter(requireActivity(),it.value?.data) {
                                binding.svName.setText(it?:"")
                                globalSearchModel?.search=it
                                globalSearchFragment()
                            }
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            binding.progressBar.visibility=View.GONE
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.visibility=View.GONE
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.popularCuisineProducts.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.rvCuisines.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvCuisines.adapter=CuisinesAdapter(requireActivity(),it.value?.data){
                                binding.svName.setText(it?:"")
                                globalSearchModel?.search=it
                                globalSearchFragment()
                            }
                            homeViewModel.newRestaurantList(requestModel)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            binding.progressBar.visibility=View.GONE
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.visibility=View.GONE
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            homeViewModel.addToFavourite.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            popularList?.get(position?:0)?.isFav=!(popularList?.get(position?:0)?.isFav!!)
                            binding.rvPopularProducts.adapter?.notifyItemChanged(position?:0)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "")
                        }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.saveRecentSearch.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            Log.e("Recent Search Saved",it?.value?.message?:"")
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })



            homeViewModel.newRestaurantList.observe(this@SearchFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.progressBar.visibility=View.GONE

                            binding.rvBrands.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
                            binding.rvBrands.adapter=BrandAdapter(requireActivity(),it.value?.data,it.value?.data)

                            if(binding?.rvBrands?.adapter?.itemCount?:0>0) binding.clBrand.visibility=View.VISIBLE
                            if(binding.rvRecentSearch.adapter?.itemCount?:0>0) binding.clRecentSearch.visibility=View.VISIBLE
                            if(binding.rvPopularProducts?.adapter?.itemCount?:0>0) binding.clPopularRestro.visibility=View.VISIBLE
                            if(binding?.rvCuisines?.adapter?.itemCount?:0>0) binding.clPopularCuisines.visibility=View.VISIBLE

                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) {
                            binding.progressBar.visibility=View.GONE
                            ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.visibility=View.GONE
                        ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    private fun globalSearchFragment() {
        if (PrefrencesHelper.getPrefrenceBooleanValue(requireActivity(), PreferenceKeyConstants.login)){
            viewModel.saveRecentSearch(SaveRecentSearch(PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id), binding.svName.text()))
            val bundle= Bundle()
            bundle.putParcelable("data",globalSearchModel)
            findNavController().navigate(R.id.globalSearch,bundle)
        }
    }


    override fun invoke(p1: Int) {
        this.position=p1
        homeViewModel?.addToFavourite(popularList?.get(position?:0)?._id, PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvRecentShowMore ->{
                if(binding.tvRecentShowMore.text?.equals("Show More")==true){
                    binding.tvRecentShowMore.text="Show Less"
                    requestModel?.limit=100
                }else{
                    binding.tvRecentShowMore.text="Show More"
                    requestModel?.limit=3
                }
                viewModel.getRecentSearch(requestModel)
            }
        }
    }


}