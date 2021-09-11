package com.saveeat.ui.fragment.main.cart

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.main.home.CommonHomeModel
import com.saveeat.model.request.order.OrderPlaceModel
import com.saveeat.model.response.saveeat.cart.DeleteItemCart
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class CartRepository@Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun cartList(model: CartRequestModel?) =safeApiCall{
        apiInterface.cartList(model,model?.token)
    }


    suspend fun updateCart(model: UpdateCartModel?) =safeApiCall{
        apiInterface.updateCart(model,model?.token)
    }

    suspend fun deleteItemFromCart(model: DeleteItemCart?) =safeApiCall{
        apiInterface.deleteItemFromCart(model,model?.token)
    }

    suspend fun orderItems(model: OrderPlaceModel?) =safeApiCall{
        apiInterface.orderItems(model,model?.token)
    }

}