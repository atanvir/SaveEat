package com.saveeat.ui.fragment.order.history

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.model.request.cart.UpdateCartModel
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.request.order.OrderPlaceModel
import com.saveeat.model.response.saveeat.cart.DeleteItemCart
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class HistoryRepository@Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun getOrderList(model: OrderHistoryModel?) =safeApiCall{
        apiInterface.getOrderList(model,model?.token)
    }

}