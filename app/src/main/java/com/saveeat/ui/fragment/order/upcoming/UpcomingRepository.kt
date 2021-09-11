package com.saveeat.ui.fragment.order.upcoming

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.order.OrderCancelModel
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class UpcomingRepository@Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun getOrderList(model: OrderHistoryModel?) =safeApiCall{
        apiInterface.getOrderList(model,model?.token)
    }


    suspend fun orderCancelByUser(model: OrderCancelModel?) =safeApiCall{
        apiInterface.orderCancelByUser(model,model?.token)
    }
}