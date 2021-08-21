package com.saveeat.ui.activity.menu.detail

import android.view.MenuItem
import com.saveeat.base.BaseRepository
import com.saveeat.model.request.menu.MenuItemDetailModel
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import javax.inject.Inject

class MenuDetailRepository @Inject constructor(private val apiInterface: SaveEatInterface): BaseRepository() {

    suspend fun getItemDetail(model: MenuItemDetailModel) = safeApiCall{
        apiInterface.getItemDetail(model,model.token)
    }
}