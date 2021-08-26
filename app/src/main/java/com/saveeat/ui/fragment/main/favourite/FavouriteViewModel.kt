package com.saveeat.ui.fragment.main.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.main.favourite.FavouriteModel
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FavouriteRepository): ViewModel() {

    private val _getFavoriteRestaurants : MutableLiveData<Resource<HomeModel?>?> = MutableLiveData()
    val getFavoriteRestaurants : LiveData<Resource<HomeModel?>?> get() = _getFavoriteRestaurants

    private val _addToFavourite : MutableLiveData<Resource<HomeModel?>?> = MutableLiveData()
    val addToFavourite : LiveData<Resource<HomeModel?>?> get() = _addToFavourite


    fun getFavoriteRestaurants(model : FavouriteModel?){
        viewModelScope.launch {
            _getFavoriteRestaurants.value=repository.getFavoriteRestaurants(model)
        }
    }

    fun addToFavourite(storeId: String?,token: String?){
        viewModelScope.launch {
            _addToFavourite.value=repository.addToFavourite(storeId,token)
        }
    }





}