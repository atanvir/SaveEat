package com.saveeat.ui.activity.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityFilterResultBinding
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.restaurant.RestaurantByLocationAdapter
import com.saveeat.ui.fragment.main.home.HomeViewModel
import com.saveeat.utils.application.CommonUtils.toolbar
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterResultActivity : BaseActivity<ActivityFilterResultBinding>(), (Int) -> Unit {

    private val viewModel : HomeViewModel by viewModels()
    private var list : MutableList<RestaurantResponseBean?>?=ArrayList()
    private var filterRequestModel : FilterRequestModel?=null
    private var position: Int=0

    override fun getActivityBinding(): ActivityFilterResultBinding = ActivityFilterResultBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        list=intent?.getParcelableArrayListExtra("data")
        filterRequestModel=intent?.getParcelableExtra<FilterRequestModel?>("filter")
        binding.rvAllRestro.layoutManager=LinearLayoutManager(this)
        binding.rvAllRestro.adapter=RestaurantByLocationAdapter(this,list,this,"Filter")
    }

    override fun initCtrl() {}

    override fun observer() {
        lifecycleScope.launch {

        viewModel.addToFavourite.observe(this@FilterResultActivity,{
            when (it) {
                is Resource.Success -> {
                    if(KeyConstants.SUCCESS==it.value?.status?:0) {
                        list?.get(position?:0)?.isFav=!(list?.get(position?:0)?.isFav!!)
                        binding.rvAllRestro.adapter?.notifyItemChanged(position?:0)
                    }
                    else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                }
                is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
            }
        })
        }
    }

    override fun invoke(p1: Int) {
        this.position=position
        viewModel?.addToFavourite(list?.get(position?:0)?._id, PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.jwtToken))
    }
}