package com.saveeat.ui.activity.menu.menu

import androidx.annotation.MenuRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.cart.CartCount
import com.saveeat.model.request.cart.CartRequestCount
import com.saveeat.model.request.filter.FilterRequestModel
import com.saveeat.model.request.menu.MenuBrandModel
import com.saveeat.model.request.menu.MenuListModel
import com.saveeat.model.request.menu.MenuModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.model.response.saveeat.order.OrderHistoryModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: MenuRepository) : ViewModel() {

    private val _getRestroDetail : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val getRestroDetail : LiveData<Resource<MenuRestaurantModel?>?>  get() = _getRestroDetail


    private val _getMenuList : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val getMenuList : LiveData<Resource<MenuRestaurantModel?>?>  get() = _getMenuList


    private val _getMenuListByFilter : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val getMenuListByFilter : LiveData<Resource<MenuRestaurantModel?>?>  get() = _getMenuListByFilter


    private val _nearByRestaurantDetail : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val nearByRestaurantDetail : LiveData<Resource<MenuRestaurantModel?>?>  get() = _nearByRestaurantDetail


    private val _addToFavourite : MutableLiveData<Resource<HomeModel?>?> = MutableLiveData()
    val addToFavourite : LiveData<Resource<HomeModel?>?> get() = _addToFavourite


    private val _cartCountParticularRestro : MutableLiveData<Resource<CartCount?>?> = MutableLiveData()
    val cartCountParticularRestro : LiveData<Resource<CartCount?>?> get() = _cartCountParticularRestro



    fun getRestroDetail(model: MenuModel){
        viewModelScope.launch {
            _getRestroDetail.value=repository.getRestroDetail(model)
        }
    }

    fun getMenuList(model: MenuListModel?) {
        viewModelScope.launch {
            _getMenuList.value=repository.getMenuList(model)
        }

    }

    fun getMenuListByFilter(model: FilterRequestModel?) {
        viewModelScope.launch {
            _getMenuListByFilter.value=repository.getMenuListByFilter(model)
        }

    }

    fun nearByRestaurantDetail(model: MenuBrandModel?) {
        viewModelScope.launch {
            _nearByRestaurantDetail.value=repository.nearByRestaurantDetail(model)
        }

    }


    fun addToFavourite(storeId: String?,token: String?){
        viewModelScope.launch {
            _addToFavourite.value=repository.addToFavourite(storeId,token)
        }
    }

    fun cartCountParticularRestro(model: CartRequestCount?) {
        viewModelScope.launch {
            _cartCountParticularRestro.value=repository.cartCountParticularRestro(model)
        }

    }
}