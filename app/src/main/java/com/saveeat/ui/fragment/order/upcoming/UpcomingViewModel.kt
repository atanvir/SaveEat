package com.saveeat.ui.fragment.order.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.order.OrderCancelModel
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel  @Inject constructor(private val repository: UpcomingRepository) : ViewModel() {
    private val _getOrderList : MutableLiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel??>?> = MutableLiveData()
    val getOrderList : LiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel??>?> get() = _getOrderList


    private val _orderCancelByUser : MutableLiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel?>?> = MutableLiveData()
    val orderCancelByUser : LiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel?>?> get() = _orderCancelByUser

    fun getOrderList(model: OrderHistoryModel?){
        viewModelScope.launch {
            _getOrderList.value=repository.getOrderList(model)
        }

    }

    fun orderCancelByUser(model: OrderCancelModel?) {
        viewModelScope.launch {
            _orderCancelByUser.value=repository.orderCancelByUser(model)
        }

    }
}