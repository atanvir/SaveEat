package com.saveeat.ui.activity.drawer.credit

import com.saveeat.base.BaseRepository
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject


class CreditRepository @Inject constructor(private var apiInterface: SaveEatInterface)  : BaseRepository() {

    suspend fun getUserDetail(token: String?)= safeApiCall {
        apiInterface.getUserDetail(token)
    }

}