package com.saveeat.ui.activity.menu.menu

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.menu.MenuListModel
import com.saveeat.model.request.menu.MenuModel
import com.saveeat.model.response.saveeat.menu.MenuRestaurantModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class MenuRepository @Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun getRestroDetail(model: MenuModel)= safeApiCall {
        apiInterface.getRestroDetail(model,model.token)
    }

    suspend fun getMenuList(model: MenuListModel) = safeApiCall{
        apiInterface.getMenuList(model,model.token)
    }

    suspend fun addToFavourite(storeId: String?,token: String?) =safeApiCall {
        apiInterface.addToFavourite(storeId,token)
    }

}