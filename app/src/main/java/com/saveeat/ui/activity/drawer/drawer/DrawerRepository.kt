package com.saveeat.ui.activity.drawer.drawer

import com.saveeat.base.BaseRepository
import com.saveeat.model.request.badge.BadgeModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class DrawerRepository @Inject constructor(private var apiInterface: SaveEatInterface) : BaseRepository() {

    suspend fun userLogout(token: String?)= safeApiCall{
        apiInterface.userLogout(token)
    }

    suspend fun userBadgesEarning(model : BadgeModel?) = safeApiCall {
        apiInterface.userBadgesEarning(model?.token)
    }
}