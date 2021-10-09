package com.saveeat.ui.activity.filter

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.RangeSlider
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityFilterBinding
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.filter.SubCategoryModel
import com.saveeat.model.request.rating.RatingStarModel
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.bean.CuisinesCategoryBean
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.filter.FilterRatingAdapter
import com.saveeat.utils.application.CommonUtils
import com.saveeat.utils.application.CommonUtils.buttonLoader
import com.saveeat.utils.application.CommonUtils.toolbar
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import com.saveeat.utils.extn.roundOffDecimal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@AndroidEntryPoint
class FilterActivity : BaseActivity<ActivityFilterBinding>(), View.OnClickListener,
    RangeSlider.OnChangeListener {
    private var ratingList: MutableList<RatingStarModel?>?=ArrayList()
    private val viewModel : FilterViewModel by viewModels()
    private var cuisineList : MutableList<CuisineBean?>?= ArrayList()
    private var cuisinesCategoryList : MutableList<CuisineBean?>?= ArrayList()

    private var minPrice : Int = 0
    private var maxPrice : Int = 2000

    private var filterRequestModel: FilterRequestModel?=null


    override fun getActivityBinding(): ActivityFilterBinding = ActivityFilterBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.clShadowButton.tvButtonLabel.text=getString(R.string.apply)
        for(i in 1..5) ratingList?.add(RatingStarModel(i,false))
        viewModel.cuisineList()
        binding.rvRating.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvRating.adapter=FilterRatingAdapter(this,ratingList) {
        for(i in ratingList?.indices!!){
            if(it!=i){ ratingList?.get(i)?.check=false }
        }
        ratingList?.get(it)?.check = !(ratingList?.get(it)?.check!!)
        binding.rvRating.adapter?.notifyDataSetChanged()
        }
    }

    override fun initCtrl() {

        binding.tvSubCategory.setOnClickListener(this)
        binding.tvCuisinesCategory.setOnClickListener(this)
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.seekbarPriceRange.addOnChangeListener(this)
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.cuisineList.observe(this@FilterActivity,{
            when (it) {
                is Resource.Success -> {
                    if(KeyConstants.SUCCESS==it.value?.status?:0) { cuisineList=it?.value?.data }
                    else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                }
                is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
            }
            })

            viewModel.restaurantFilter.observe(this@FilterActivity,{
                buttonLoader(binding.clShadowButton, false)
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            startActivity(Intent(this@FilterActivity,FilterResultActivity::class.java).putParcelableArrayListExtra("data",ArrayList(it.value?.data)).putExtra("filter",filterRequestModel))
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })

            viewModel.cusineSubCategory.observe(this@FilterActivity,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            cuisinesCategoryList=it?.value?.data
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> {  ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvSubCategory -> {
                val intent=Intent(this@FilterActivity,FilterCategoryActivity::class.java)
                intent.putExtra("name","Sub Category")
                intent.putParcelableArrayListExtra("data",ArrayList(cuisinesCategoryList))
                subCategoryLaucher.launch(intent)
            }
            R.id.tvCuisinesCategory -> {
                val intent=Intent(this@FilterActivity,FilterCategoryActivity::class.java)
                intent.putExtra("name","Cuisines")
                intent.putParcelableArrayListExtra("data",ArrayList(cuisineList))
                cuisineslaucher.launch(intent)
            }

            R.id.ivButton -> {
                buttonLoader(binding.clShadowButton, true)
                filterRequestModel=FilterRequestModel(cuisines = getSelectedCuisines(),
                                                      distance = /*PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.distance)*/"1000",
                                                      foodType=getFoodType(),
                                                      latitude = PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.latitude).toDouble(),
                                                      longitude = PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.longitude).toDouble(),
                                                      maxPrice = maxPrice, minPrice=minPrice,
                                                      pickupLaterAllowance =binding.cbPickUpAllowance?.isChecked,
                                                      rating = getSelectedRating(),
                                                      subCategory = getSelectedSubCategory(),
                                                      token = PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants.jwtToken),
                                                      userId =PrefrencesHelper.getPrefrenceStringValue(this, PreferenceKeyConstants._id),menuId = "")
                viewModel.restaurantFilter(filterRequestModel)
            }
        }
    }

    private fun getSelectedCuisines(): List<String> {
       var returnList : MutableList<String> = ArrayList()
       for(i in cuisineList?.indices!!){
           if(cuisineList?.get(i)?.check==true){
               returnList?.add(cuisineList?.get(i)?.name?:"")
           }
       }
       return returnList

    }

    private fun getSelectedSubCategory(): List<String> {
        var returnList : MutableList<String> = ArrayList()
        for(i in cuisinesCategoryList?.indices!!){
            if(cuisinesCategoryList?.get(i)?.check==true){
                returnList?.add(cuisinesCategoryList?.get(i)?.name?:"")
            }
        }
        return returnList
    }


    private var cuisineslaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== RESULT_OK) {
            cuisineList=it.data?.getParcelableArrayListExtra<CuisineBean?>("data")
            var list : MutableList<String?>?= ArrayList()
            var cuisinesDataId : MutableList<String?>?= ArrayList()
            for(i in cuisineList?.indices!!){ if(cuisineList?.get(i)?.check==true) {
                list?.add(cuisineList?.get(i)?.name)
                cuisinesDataId?.add(cuisineList?.get(i)?._id)
            } }
            if(list?.isNotEmpty()==true) binding.tvCuisinesCategory.text=TextUtils.join(", ", list!!)
            else binding.tvCuisinesCategory.text=""

            viewModel.cusineSubCategory(SubCategoryModel(cuisineData = cuisinesDataId,userId = "",token = ""))
        }
    }


    private var subCategoryLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== RESULT_OK) {
            cuisinesCategoryList=it.data?.getParcelableArrayListExtra<CuisineBean?>("data")
            val list : MutableList<String?>?= ArrayList()
            for(i in cuisinesCategoryList?.indices!!){ if(cuisinesCategoryList?.get(i)?.check==true){

                list?.add(cuisinesCategoryList?.get(i)?.name) }
            }

            if(list?.isNotEmpty()==true) binding.tvSubCategory.text=TextUtils.join(", ", list!!)
            else binding.tvSubCategory.text=""
        }
    }


    private fun getSelectedRating(): Int {
        var rating=0
        for(i in ratingList?.indices!!){
            if(ratingList?.get(i)?.check==true) {
                rating=ratingList?.get(i)?.star?.toInt()?:0
                break
            }
        }
        return rating
    }

    private fun getFoodType(): String {
        if(binding.cbVeg?.isChecked && binding.cbNonVeg?.isChecked) return KeyConstants.BOTH
        else if(binding.cbVeg?.isChecked) return KeyConstants.VEG
        else if(binding.cbNonVeg?.isChecked) return KeyConstants.NON_VEG
        else return KeyConstants.BOTH
    }

    override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
        var minPerPrice=20
        var maxPerPrice=20

        minPrice=(slider?.values?.get(0)?.toDouble()?.times(minPerPrice.toDouble()))?.roundToInt()!!
        maxPrice=(slider?.values?.get(1)?.toDouble()?.times(maxPerPrice.toDouble()))?.roundToInt()!!
        Log.e("seekbar","min "+slider?.values?.get(0)?.toDouble())
        Log.e("seekbar","max "+slider?.values?.get(1)?.toDouble())
        Log.e("seekbar","minPrice "+minPerPrice?.toDouble())
        Log.e("seekbar","maxPrice "+maxPerPrice?.toDouble())

        binding.tvMinPrice.text= getString(R.string.price,minPrice.toString())
        binding.tvMaxPrice.text= getString(R.string.price,maxPrice.toString())

    }


}