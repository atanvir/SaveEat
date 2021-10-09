package com.saveeat.ui.activity.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.filter.SubCategoryModel
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.main.filter.FilterCuisinesModel
import com.saveeat.model.response.saveeat.main.filter.FilterSubCategoryModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class FilterViewModel @Inject constructor(private val repository: FilterRepository) : ViewModel() {

    private val _cuisineList : MutableLiveData<Resource<FilterCuisinesModel?>?> = MutableLiveData()
    val cuisineList : LiveData<Resource<FilterCuisinesModel?>?> get() = _cuisineList


    private val _cusineSubCategory : MutableLiveData<Resource<FilterCuisinesModel?>?> = MutableLiveData()
    val cusineSubCategory : LiveData<Resource<FilterCuisinesModel?>?> get() = _cusineSubCategory



    private val _restaurantFilter : MutableLiveData<Resource<HomeModel?>?> = MutableLiveData()
    val restaurantFilter : LiveData<Resource<HomeModel?>?> get() = _restaurantFilter


    fun cuisineList() {
        viewModelScope.launch {
            _cuisineList.value=repository.cuisineList()
        }

    }

    fun cusineSubCategory(model: SubCategoryModel?) {
        viewModelScope.launch {
            _cusineSubCategory.value=repository.cuisineSubCategory(model)
        }

    }

    fun restaurantFilter(model: FilterRequestModel?){
        viewModelScope.launch {
            _restaurantFilter.value=repository.restaurantFilter(model)
        }
    }

}