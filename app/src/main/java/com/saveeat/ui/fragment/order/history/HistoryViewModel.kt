package com.saveeat.ui.fragment.order.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.ui.activity.profile.ProfileRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: HistoryRepository) : ViewModel() {
    private val _getOrderList : MutableLiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel??>?> = MutableLiveData()
    val getOrderList : LiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel??>?> get() = _getOrderList

    fun getOrderList(model: OrderHistoryModel?){
        viewModelScope.launch {
            _getOrderList.value=repository.getOrderList(model)
        }

    }
}