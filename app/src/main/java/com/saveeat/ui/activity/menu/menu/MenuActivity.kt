package com.saveeat.ui.activity.menu.menu

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityMenuBinding
import com.saveeat.model.request.menu.MenuListModel
import com.saveeat.model.request.menu.MenuModel
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.menu.MenuItemProductModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.home.RestaurantHomeAdapter
import com.saveeat.ui.adapter.menu.MenuCategoryAdapter
import com.saveeat.ui.adapter.menu.MenuProductAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter
import com.saveeat.utils.application.*
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri
import com.saveeat.BuildConfig


@AndroidEntryPoint
class MenuActivity : BaseActivity<ActivityMenuBinding>(), View.OnClickListener, (Int) -> Unit {

    private val viewModel : MenuViewModel by viewModels()
    private var menuProductDataList:  MutableList<MenuItemProductModel?>?= ArrayList()
    private var cuisineList:  MutableList<CuisineBean?>?= ArrayList()


    override fun getActivityBinding(): ActivityMenuBinding = ActivityMenuBinding.inflate(layoutInflater)

    override fun inits() {
        if(intent.getStringExtra("type").equals(KeyConstants.RESTAURANT,true)) {
            binding.clShimmer.shimmerContainer.startShimmer()

            viewModel.getMenuList(MenuListModel(menuId = intent.getStringExtra("_id"),
                                                latitude= PrefrencesHelper.getPrefrenceStringValue(this@MenuActivity, PreferenceKeyConstants.latitude),
                                                longitude= PrefrencesHelper.getPrefrenceStringValue(this@MenuActivity, PreferenceKeyConstants.longitude),
                                                userId = PrefrencesHelper.getPrefrenceStringValue(this@MenuActivity, PreferenceKeyConstants._id),
                                                token = PrefrencesHelper.getPrefrenceStringValue(this@MenuActivity, PreferenceKeyConstants.jwtToken)))

        }
    }

    override fun initCtrl() {

        binding.cpType.setOnClickListener(this)
        binding.cpType.setOnCloseIconClickListener {
            binding.cpType.isCloseIconVisible=false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.cpType.chipBackgroundColor=getColorStateList(R.color.white)
                binding.cpType.closeIconTint=getColorStateList(R.color.black)
            }
            binding.cpType.setTextColor(ContextCompat.getColor(this,R.color.black))
        }
        binding.ivBack.setOnClickListener(this)
        binding.clFooter.rlDirection.setOnClickListener(this)
        binding.clFooter.rlPhoneCall.setOnClickListener(this)
        binding.clFooter.rlShareApp.setOnClickListener(this)
        binding.ivFavMenu.setOnClickListener(this)


        binding.appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {

                if (State.COLLAPSED == state) {
                    binding.cvMain.visibility=View.INVISIBLE
                    binding.tvToolbarHeader.visibility=View.VISIBLE
                    binding.ivRestroImage.visibility=View.VISIBLE

                } else if (State.IDLE == state) {
                    binding.cvMain.visibility=View.VISIBLE
                    binding.tvToolbarHeader.visibility=View.INVISIBLE
                    binding.ivRestroImage.visibility=View.INVISIBLE
                }
            }
        })
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.getMenuList.observe(this@MenuActivity,{
                stopShimmer()
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding.model=it.value?.data

                            cuisineList=it?.value?.data?.cuisineList
                            cuisineList?.add(0,CuisineBean(check = true,_id = "123",name = "All"))
                            binding.rvMenuCategories.layoutManager=LinearLayoutManager(this@MenuActivity,LinearLayoutManager.HORIZONTAL,false)
                            binding.rvMenuCategories.adapter=MenuCategoryAdapter(this@MenuActivity,cuisineList,this@MenuActivity)

                            menuProductDataList=it.value?.data?.itemData
                            menuProductDataList?.add(0,MenuItemProductModel(itemList = it.value?.data?.itemListAll,cuisineId = "123",cuisineName = "All"))

                            binding.rvProducts.layoutManager=GridLayoutManager(this@MenuActivity,2)
                            binding.rvProducts.adapter= MenuProductAdapter(this@MenuActivity,getSelectedCategoryData(0))
                            binding.clFooter.clFootMain.visibility=View.VISIBLE

                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer(); ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.addToFavourite.observe(this@MenuActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            binding?.model?.restroObj?.restroData?.isFav=!(binding?.model?.restroObj?.restroData?.isFav!!)
                            binding.model=binding.model
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

    private fun getSelectedCategoryData(position: Int):  MutableList<RestaurantResponseBean?>? {
        var list:  MutableList<RestaurantResponseBean?>?= ArrayList()
        for(i in menuProductDataList?.indices!!){
         if(menuProductDataList?.get(i)?.cuisineId?.equals(cuisineList?.get(position)?._id.toString(),ignoreCase = true)==true){
             list= menuProductDataList?.get(i)?.itemList
             break
         }
        }
        return list
    }

    private fun stopShimmer(){
        binding.clShimmer.shimmerContainer.stopShimmer()
        binding.clShimmer.shimmerContainer.visibility=View.GONE
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.ivFavMenu ->{
                viewModel.addToFavourite(binding.ivFavMenu.tag as String,PrefrencesHelper.getPrefrenceStringValue(this@MenuActivity, PreferenceKeyConstants.jwtToken))
            }

            R.id.rlShareApp ->{
                val sendIntent =  Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                sendIntent.setType("text/plain")
                startActivity(sendIntent)
            }

            R.id.rlDirection -> {
                CommonUtils.openGoogleMap(this,(binding.clFooter.rlDirection.tag as String).split(",")[0].toDouble(),(binding.clFooter.rlDirection.tag as String).split(",")[1].toDouble())
            }

            R.id.rlPhoneCall ->{
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.clFooter.tvPhoneCall.text.toString()))
                startActivity(intent)
            }


            R.id.ivBack ->{ onBackPressed() }
            R.id.cpType ->{
                binding.cpType.isCloseIconVisible=true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.cpType.chipBackgroundColor=this.getColorStateList(R.color.app_theme)
                    binding.cpType.closeIconTint=this.getColorStateList(R.color.white)
                }
                binding.cpType.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }

    override fun invoke(position: Int) {
        binding.rvMenuCategories.smoothScrollToPosition(position)
        binding.nestedSvProduct.scrollTo(0,0)
        (binding.rvProducts.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        binding.rvProducts.layoutManager=GridLayoutManager(this@MenuActivity,2)
        binding.rvProducts.adapter= MenuProductAdapter(this@MenuActivity,getSelectedCategoryData(position))
    }


}