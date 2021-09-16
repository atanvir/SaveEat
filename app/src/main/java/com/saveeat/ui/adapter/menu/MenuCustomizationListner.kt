package com.saveeat.ui.adapter.menu

import com.saveeat.model.request.restaurant.CategoryModel
import com.saveeat.model.response.saveeat.menu.MenuCategoryModel

interface MenuCustomizationListner {
    fun onMenuSingleItemClick(position: Int?,data: MenuCategoryModel?)
    fun onMenuMultipleItemClick(position: Int?,data: MenuCategoryModel?)
}