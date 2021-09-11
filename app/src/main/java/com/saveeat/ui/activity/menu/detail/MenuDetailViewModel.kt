package com.saveeat.ui.activity.menu.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(private val repository: MenuDetailRepository) : ViewModel() {

    private val _getItemDetail : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val getItemDetail : LiveData<Resource<MenuRestaurantModel?>?> get() =_getItemDetail


    private val _addToCart : MutableLiveData<Resource<CartModel?>?> = MutableLiveData()
    val addToCart : LiveData<Resource<CartModel?>?> get() =_addToCart


    private val _updateCart : MutableLiveData<Resource<CartModel?>?> = MutableLiveData()
    val updateCart : LiveData<Resource<CartModel?>?> get() =_updateCart


    fun getItemDetail(model: MenuItemDetailModel){
        viewModelScope.launch {
            _getItemDetail.value=repository.getItemDetail(model)
        }

    }

    fun addToCart(modelAddTo: com.saveeat.model.request.cart.AddToCartModel?) {
        viewModelScope.launch {
            _addToCart.value=repository.addToCart(modelAddTo)
        }

    }

    fun updateCart(model: UpdateCartModel?) {
        viewModelScope.launch {
            _updateCart.value=repository.updateCart(model)
        }
    }

}