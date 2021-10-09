package com.saveeat.ui.fragment.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.main.search.GlobalSearchModel
import com.saveeat.model.request.main.search.SaveRecentSearch
import com.saveeat.model.response.saveeat.main.home.HomeModel
import com.saveeat.model.response.saveeat.main.search.SearchModel
import com.saveeat.ui.fragment.main.home.HomeRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository): ViewModel() {

    private val _restaurantList : MutableLiveData<Resource<HomeModel?>?> = MutableLiveData()
    val restaurantList : LiveData<Resource<HomeModel?>?> get() = _restaurantList


    private val _saveRecentSearch : MutableLiveData<Resource<SearchModel?>?> = MutableLiveData()
    val saveRecentSearch : LiveData<Resource<SearchModel?>?> get() = _saveRecentSearch


    private val _getRecentSearch : MutableLiveData<Resource<SearchModel?>?> = MutableLiveData()
    val getRecentSearch : LiveData<Resource<SearchModel?>?> get() = _getRecentSearch



    private val _searchSuggestions : MutableLiveData<Resource<com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?>?> = MutableLiveData()
    val searchSuggestions : LiveData<Resource<com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?>?> get() = _searchSuggestions


    private val _globalSearch : MutableLiveData<Resource<com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?>?> = MutableLiveData()
    val globalSearch : LiveData<Resource<com.saveeat.model.response.saveeat.main.search.GlobalSearchModel?>?> get() = _globalSearch


    private val _popularCuisineProducts : MutableLiveData<Resource<SearchModel?>?> = MutableLiveData()
    val popularCuisineProducts : LiveData<Resource<SearchModel?>?> get() = _popularCuisineProducts


    fun restaurantList(model: CommonHomeModel?){
        viewModelScope.launch {
            _restaurantList.value=repository.restaurantList(model)
        }
    }


    fun getRecentSearch(model: CommonHomeModel?){
        viewModelScope.launch {
            _getRecentSearch.value=repository.getRecentSearch(model)
        }
    }


    fun popularCuisineProducts(model: CommonHomeModel?) {
        viewModelScope.launch {
            _popularCuisineProducts.value=repository.popularCuisineProducts(model)
        }
    }

    fun searchSuggestions(model: GlobalSearchModel?){
        viewModelScope.launch {
            _searchSuggestions.value=repository.searchSuggestions(model)
        }
    }

    fun saveRecentSearch(model : SaveRecentSearch?){
        viewModelScope.launch {
            _saveRecentSearch.value=repository.saveRecentSearch(model)
        }
    }


    fun globalSearch(model: GlobalSearchModel?){
        viewModelScope.launch {
            _globalSearch.value=repository.globalSearch(model)
        }
    }
}