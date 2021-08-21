package com.saveeat.ui.fragment.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {

    private val _moreSaveProductList : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val moreSaveProductList : LiveData<Resource<HomeModel?>> get() = _moreSaveProductList


    private val _restaurantForHomeList : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val restaurantForHomeList : LiveData<Resource<HomeModel?>> get() = _restaurantForHomeList


    private val _popularRestaurantList : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val popularRestaurantList : LiveData<Resource<HomeModel?>> get() = _popularRestaurantList


    private val _newRestaurantList : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val newRestaurantList : LiveData<Resource<HomeModel?>> get() = _newRestaurantList


    private val _addToFavourite : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val addToFavourite : LiveData<Resource<HomeModel?>> get() = _addToFavourite

    fun moreSaveProductList(model: CommonHomeModel?){
        viewModelScope.launch {
            _moreSaveProductList.value=repository.moreSaveProductList(model)
        }
    }

    fun popularRestaurantList(model: CommonHomeModel?) {
        viewModelScope.launch {
            _popularRestaurantList.value=repository.popularRestaurantList(model)
        }
    }

    fun newRestaurantList(model: CommonHomeModel?){
        viewModelScope.launch {
            _newRestaurantList.value=repository.newRestaurantList(model)
        }
    }




    fun restaurantForHomeList(model: CommonHomeModel?){
        viewModelScope.launch {
            _restaurantForHomeList.value=repository.restaurantForHomeList(model)
        }
    }

    fun addToFavourite(storeId: String?,token: String?){
        viewModelScope.launch {
            _addToFavourite.value=repository.addToFavourite(storeId,token)
        }
    }


}