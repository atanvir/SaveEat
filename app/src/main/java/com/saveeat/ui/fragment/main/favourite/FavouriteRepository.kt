package com.saveeat.ui.fragment.main.favourite

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.main.favourite.FavouriteModel
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class FavouriteRepository @Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {


    suspend fun getFavoriteRestaurants(model: FavouriteModel?) =safeApiCall {
        apiInterface.getFavoriteRestaurants(model,model?.token)
    }

    suspend fun addToFavourite(storeId: String?,token: String?) =safeApiCall {
        apiInterface.addToFavourite(storeId,token)
    }


}