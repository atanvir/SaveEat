package com.saveeat.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.cart.CartCount
import com.saveeat.model.response.saveeat.cart.CartModel
import com.saveeat.ui.fragment.main.cart.CartRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _getCartCount : MutableLiveData<Resource<CartCount??>?> = MutableLiveData()
    val getCartCount : LiveData<Resource<CartCount??>?> get() = _getCartCount


    fun getCartCount(token: String?){
        viewModelScope.launch {
            _getCartCount.value=repository.getCartCount(token)
        }
    }
}