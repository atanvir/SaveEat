package com.saveeat.ui.fragment.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.order.OrderPlaceModel
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.model.response.saveeat.cart.DeleteItemCart
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.ui.fragment.main.home.HomeRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository): ViewModel() {
    private val _getCartItem : MutableLiveData<Resource<CartModel??>?> = MutableLiveData()
    val getCartItem : LiveData<Resource<CartModel??>?> get() = _getCartItem


    private val _updateCart : MutableLiveData<Resource<CartModel??>?> = MutableLiveData()
    val updateCart : LiveData<Resource<CartModel??>?> get() = _updateCart



    private val _deleteItemFromCart : MutableLiveData<Resource<CartModel??>?> = MutableLiveData()
    val deleteItemFromCart : LiveData<Resource<CartModel??>?> get() = _deleteItemFromCart



    private val _orderItems : MutableLiveData<Resource<CartModel??>?> = MutableLiveData()
    val orderItems : LiveData<Resource<CartModel??>?> get() = _orderItems

    fun cartList(model: CartRequestModel?){
        viewModelScope.launch {
            _getCartItem.value=repository.cartList(model)
        }
    }

    fun updateCart(model: UpdateCartModel?) {
        viewModelScope.launch {
            _updateCart.value=repository.updateCart(model)
        }
    }



    fun deleteItemFromCart(model: DeleteItemCart?) {
        viewModelScope.launch {
            _deleteItemFromCart.value=repository.deleteItemFromCart(model)
        }
    }

    fun orderItems(model: OrderPlaceModel?) {
        viewModelScope.launch {
            _orderItems.value=repository.orderItems(model)
        }

    }
}