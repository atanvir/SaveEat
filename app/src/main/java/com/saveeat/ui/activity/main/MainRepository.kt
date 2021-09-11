package com.saveeat.ui.activity.main

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.cart.CartRequestModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class MainRepository@Inject constructor(private val apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun getCartCount(token: String?) =safeApiCall {
        apiInterface.getCartCount(token)
    }
}