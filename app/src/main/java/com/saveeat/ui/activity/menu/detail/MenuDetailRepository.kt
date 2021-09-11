package com.saveeat.ui.activity.menu.detail

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.cart.AddToCartModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class MenuDetailRepository @Inject constructor(private val apiInterface: SaveEatInterface): BaseRepository() {

    suspend fun getItemDetail(model: MenuItemDetailModel) = safeApiCall{
        apiInterface.getItemDetail(model,model.token)
    }


    suspend fun addToCart(modelAddTo: AddToCartModel?) = safeApiCall{
        apiInterface.addToCart(modelAddTo,modelAddTo?.token)
    }


    suspend fun updateCart(modelAddTo: UpdateCartModel?) = safeApiCall{
        apiInterface.updateCart(modelAddTo,modelAddTo?.token)
    }
}