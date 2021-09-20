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
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.menu.MenuItemProductModel
import com.saveeat.ui.adapter.menu.MenuCategoryAdapter
import com.saveeat.ui.adapter.menu.MenuProductAdapter
import com.saveeat.utils.application.*
import com.saveeat.utils.helper.AppBarStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.saveeat.BuildConfig
import com.saveeat.model.request.cart.CartRequestCount
import com.saveeat.model.request.menu.MenuBrandModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.jwtToken
import com.saveeat.repository.cache.PreferenceKeyConstants.latitude
import com.saveeat.repository.cache.PreferenceKeyConstants.longitude
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.ui.activity.main.MainActivity
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter
import com.saveeat.ui.bottomsheet.restaurant.RestaurantBottomSheet
import com.saveeat.utils.application.KeyConstants.BOTH
import com.saveeat.utils.application.KeyConstants.BRAND
import com.saveeat.utils.application.KeyConstants.RESTAURANT
import com.saveeat.utils.application.KeyConstants.VEG
import com.saveeat.utils.extn.queryChanged


@AndroidEntryPoint
class MenuActivity : BaseActivity<ActivityMenuBinding>(), View.OnClickListener, (Int) -> Unit {

    private val viewModel : MenuViewModel by viewModels()
    private var menuProductDataList:  MutableList<MenuItemProductModel?>?= ArrayList()
    private var cuisineList:  MutableList<CuisineBean?>?= ArrayList()
    private var cartCountRestro : Boolean?=false

    var cloneItemList: MutableList<RestaurantResponseBean?>? =ArrayList()
    var cloneFullPriceList: MutableList<RestaurantResponseBean?>? =ArrayList()


    override fun getActivityBinding(): ActivityMenuBinding = ActivityMenuBinding.inflate(layoutInflater)

    override fun inits() {

//        startShimmerAnimation()
        binding.clShimmer.shimmerContainer.visibility=View.VISIBLE
        binding.clShimmer.shimmerContainer.startShimmer()

        if(intent.getStringExtra("type").equals(RESTAURANT,true)) {
            viewModel.getMenuList(MenuListModel(menuId = intent.getStringExtra("_id"),
                                                latitude= getPrefrenceStringValue(this@MenuActivity, latitude),
                                                longitude= getPrefrenceStringValue(this@MenuActivity, longitude),
                                                userId = getPrefrenceStringValue(this@MenuActivity, _id),
                                                token = getPrefrenceStringValue(this@MenuActivity, jwtToken),
                                                foodType = BOTH))
        }
        else if(intent?.getStringExtra("type").equals(BRAND,ignoreCase = true)){
            viewModel.nearByRestaurantDetail(MenuBrandModel(longitude= getPrefrenceStringValue(this@MenuActivity, longitude),
                                                            latitude= getPrefrenceStringValue(this@MenuActivity, latitude),
                                                            userId = getPrefrenceStringValue(this@MenuActivity, _id),
                                                            brandId = intent?.getStringExtra("_id"),
                                                            token = getPrefrenceStringValue(this@MenuActivity, jwtToken),
                                                            foodType = BOTH))
        }
    }

    private fun startShimmerAnimation(){
//        cuisineList?.clear()
//        menuProductDataList?.clear()
        binding.clShimmer.shimmerContainer.visibility=View.VISIBLE
        binding.clShimmer.shimmerContainer.startShimmer()

    }

    override fun onResume() {
        super.onResume()
        if(cartCountRestro==true) viewModel.cartCountParticularRestro(CartRequestCount(restaurantId=binding?.model?.restroObj?.restroData?._id,token = getPrefrenceStringValue(this@MenuActivity, jwtToken),userId = getPrefrenceStringValue(this@MenuActivity, _id)))

    }


    override fun initCtrl() {

        binding.edSeach.queryChanged {
            if(binding.rvSellingPrice.adapter!=null) (binding.rvSellingPrice.adapter as MenuProductAdapter).filter.filter(it)
            if(binding.rvProducts.adapter!=null) (binding.rvProducts.adapter as MenuProductAdapter).filter.filter(it)
        }
        binding.cpType.setOnClickListener(this)
        binding.tvProductName.setOnClickListener(this)
        binding.clViewCart.setOnClickListener(this)
        binding.cpType.setOnCloseIconClickListener {
            //startShimmerAnimation()
            viewModel.getMenuList(MenuListModel(menuId = intent.getStringExtra("_id"),
                                                latitude= getPrefrenceStringValue(this@MenuActivity, latitude),
                                                longitude= getPrefrenceStringValue(this@MenuActivity, longitude),
                                                userId = getPrefrenceStringValue(this@MenuActivity, _id),
                                                token = getPrefrenceStringValue(this@MenuActivity, jwtToken),
                                                foodType = BOTH))
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
                            var data=it.value?.data
                            data?.menu=true
                            binding.model=data

                            cuisineList=it?.value?.data?.cuisineList
                            cuisineList?.add(0,CuisineBean(check = true,_id = "123",name = "All"))
                            binding.rvMenuCategories.layoutManager=LinearLayoutManager(this@MenuActivity,LinearLayoutManager.HORIZONTAL,false)
                            binding.rvMenuCategories.adapter=MenuCategoryAdapter(this@MenuActivity,cuisineList,this@MenuActivity)
                            menuProductDataList=it.value?.data?.itemData
                            menuProductDataList?.add(0,MenuItemProductModel(itemList = it.value?.data?.itemListAll,cuisineId = "123",cuisineName = "Selling Price",fullPriceItems = it.value?.data?.fullPriceItemsList))


                            loadDataByIndex(0)
                            binding.clFooter.clFootMain.visibility=View.VISIBLE
                            if(cartCountRestro==false){
                                viewModel.cartCountParticularRestro(CartRequestCount(restaurantId=binding?.model?.restroObj?.restroData?._id,token = getPrefrenceStringValue(this@MenuActivity, jwtToken),userId = getPrefrenceStringValue(this@MenuActivity, _id)))
                            }
                            cartCountRestro=true

                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer(); ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
            viewModel.cartCountParticularRestro.observe(this@MenuActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            if(it?.value?.data?:0.0>0.0){
                                binding.clViewCart.visibility=View.VISIBLE
                                if(it.value?.data?:0.0>1.0) binding.tvItemCount.text="${Math.round(it.value?.data?:0.0)} Items"
                                else binding.tvItemCount.text="${Math.round(it.value?.data?:0.0)} Item"
                            }
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { stopShimmer(); ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { stopShimmer(); ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })


            viewModel.nearByRestaurantDetail.observe(this@MenuActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            viewModel.getMenuList(MenuListModel(menuId = it.value?.data?.menuData,
                                                                latitude= getPrefrenceStringValue(this@MenuActivity, latitude),
                                                                longitude= getPrefrenceStringValue(this@MenuActivity, longitude),
                                                                userId = getPrefrenceStringValue(this@MenuActivity, _id),
                                                                token = getPrefrenceStringValue(this@MenuActivity, jwtToken),
                                                                foodType = BOTH))
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
                            binding?.model?.restroObj?.isFav = it?.value?.message?.contains("Remove",ignoreCase = true) != true
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

    private fun loadDataByIndex(position: Int) {
        cloneItemList=getSelectedCategoryData(position)?.get(0)?.itemList
        cloneFullPriceList=getSelectedCategoryData(position)?.get(0)?.fullPriceItems


        if(menuProductDataList?.get(position)?.itemList?.size?:0==0) binding.tvSellingPrice.visibility=View.GONE
        else binding.tvSellingPrice.visibility=View.GONE
        binding.rvSellingPrice.layoutManager=GridLayoutManager(this@MenuActivity,2)
        binding.rvSellingPrice.adapter= MenuProductAdapter(this@MenuActivity,getSelectedCategoryData(position)?.get(0)?.itemList,cloneItemList,"Selling")

        if(menuProductDataList?.get(position)?.fullPriceItems?.size?:0==0) binding.tvTotalPrice.visibility=View.GONE
        else binding.tvTotalPrice.visibility=View.VISIBLE


        binding.rvProducts.layoutManager=GridLayoutManager(this@MenuActivity,2)
        binding.rvProducts.adapter= MenuProductAdapter(this@MenuActivity,getSelectedCategoryData(position)?.get(0)?.fullPriceItems,cloneFullPriceList,"Fix")

        if(binding.edSeach.query.length>0){
            if(binding.rvSellingPrice.adapter!=null) (binding.rvSellingPrice.adapter as MenuProductAdapter).filter.filter(binding.edSeach.query)
            if(binding.rvProducts.adapter!=null) (binding.rvProducts.adapter as MenuProductAdapter).filter.filter(binding.edSeach.query)
        }
    }

    private fun getSelectedCategoryData(position: Int):  MutableList<MenuItemProductModel?>? {
        var list:  MutableList<MenuItemProductModel?>?= ArrayList()
        for(i in menuProductDataList?.indices!!){
         if(menuProductDataList?.get(i)?.cuisineId?.equals(cuisineList?.get(position)?._id.toString(),ignoreCase = true)==true){
             list?.add(menuProductDataList?.get(i))
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

            R.id.clViewCart ->{
               startActivity(Intent(this,MainActivity::class.java).putExtra("menu",true))
            }

            R.id.tvProductName ->{
                val bottomSheet = RestaurantBottomSheet()
                val bundle = Bundle()
                bundle.putParcelable("data", binding.model)
                bottomSheet.arguments = bundle
                bottomSheet.show(supportFragmentManager, "")
            }

            R.id.ivFavMenu ->{
                viewModel.addToFavourite(binding.ivFavMenu.tag as String,getPrefrenceStringValue(this@MenuActivity, jwtToken))
            }

            R.id.rlShareApp ->{
                val sendIntent =  Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                sendIntent.type = "text/plain"
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
//                startShimmerAnimation()
                viewModel.getMenuList(MenuListModel(menuId = intent.getStringExtra("_id"),
                                                    latitude= getPrefrenceStringValue(this@MenuActivity, latitude),
                                                    longitude= getPrefrenceStringValue(this@MenuActivity, longitude),
                                                    userId = getPrefrenceStringValue(this@MenuActivity, _id),
                                                    token = getPrefrenceStringValue(this@MenuActivity, jwtToken),
                                                    foodType = VEG))
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
        (binding.rvSellingPrice.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        loadDataByIndex(position)
    }


}