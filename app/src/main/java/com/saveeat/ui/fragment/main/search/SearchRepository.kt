package com.saveeat.ui.fragment.main.search

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.main.search.GlobalSearchModel
import com.saveeat.model.request.main.search.SaveRecentSearch
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class SearchRepository @Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun restaurantList(model: CommonHomeModel?) = safeApiCall{
        apiInterface.restaurantList(model,model?.token)
    }

    suspend fun popularCuisineProducts(model: CommonHomeModel?) = safeApiCall{
        apiInterface.popularCuisineProducts(model,model?.token)
    }


    suspend fun getRecentSearch(model: CommonHomeModel?) = safeApiCall{
        apiInterface.getRecentSearch(model,model?.token)
    }


    suspend fun searchSuggestions(model: GlobalSearchModel?) = safeApiCall{
        apiInterface.searchSuggestions(model,model?.token)
    }

    suspend fun saveRecentSearch(model: SaveRecentSearch?) = safeApiCall{
        apiInterface.saveRecentSearch(model)
    }

    suspend fun globalSearch(model: GlobalSearchModel?) = safeApiCall {
        apiInterface.globalSearch(model,model?.token)
    }
}