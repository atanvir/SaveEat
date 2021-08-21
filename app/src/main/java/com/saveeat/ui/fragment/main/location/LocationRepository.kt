package com.saveeat.ui.fragment.main.location

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class LocationRepository @Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

   suspend fun restaurantList(model: CommonHomeModel?) = safeApiCall{
       apiInterface.restaurantList(model,model?.token)
   }

    suspend fun addToFavourite(storeId: String?,token: String?) =safeApiCall {
        apiInterface.addToFavourite(storeId,token)
    }


}