package com.saveeat.ui.fragment.main.location

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
class LocationViewModel  @Inject constructor(private val repository: LocationRepository): ViewModel() {

    private val _restaurantList : MutableLiveData<Resource<HomeModel??>> = MutableLiveData()
    val restaurantList : LiveData<Resource<HomeModel?>> get() = _restaurantList

    private val _addToFavourite : MutableLiveData<Resource<HomeModel?>> = MutableLiveData()
    val addToFavourite : LiveData<Resource<HomeModel?>> get() = _addToFavourite


    fun addToFavourite(storeId: String?,token: String?){
        viewModelScope.launch {
            _addToFavourite.value=repository.addToFavourite(storeId,token)
        }
    }


    fun restaurantList(model: CommonHomeModel?){
        viewModelScope.launch {
            _restaurantList.value=repository.restaurantList(model)
        }
    }
}