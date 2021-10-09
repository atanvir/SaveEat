package com.saveeat.ui.fragment.main.home

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class HomeRepository @Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

        suspend fun moreSaveProductList(model: CommonHomeModel?) =safeApiCall{
            apiInterface.moreSaveProductList(model,model?.token)
        }


    suspend fun restaurantForHomeList(model: CommonHomeModel?) =safeApiCall{
        apiInterface.restaurantForHomeList(model,model?.token)
    }

    suspend fun popularRestaurantList(model: CommonHomeModel?) =safeApiCall{
        apiInterface.popularRestaurantList(model,model?.token)
    }

    suspend fun newRestaurantList(model: CommonHomeModel?) =safeApiCall{
        apiInterface.newRestaurantList(model,model?.token)
    }

    suspend fun addToFavourite(storeId: String?,token: String?) =safeApiCall {
        apiInterface.addToFavourite(storeId,token)
    }






}