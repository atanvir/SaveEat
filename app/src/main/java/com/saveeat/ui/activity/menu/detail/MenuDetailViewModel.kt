package com.saveeat.ui.activity.menu.detail

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(private val repository: MenuDetailRepository) : ViewModel() {

    private val _getItemDetail : MutableLiveData<Resource<MenuRestaurantModel?>?> = MutableLiveData()
    val getItemDetail : LiveData<Resource<MenuRestaurantModel?>?> get() =_getItemDetail

    fun getItemDetail(model: MenuItemDetailModel){
        viewModelScope.launch {
            _getItemDetail.value=repository.getItemDetail(model)
        }

    }

}