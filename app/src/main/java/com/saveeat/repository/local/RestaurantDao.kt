package com.saveeat.repository.local

import androidx.room.Dao
import androidx.room.Query
import com.saveeat.base.BaseDao

@Dao
interface RestaurantDao : BaseDao<RestaurantEntity> {
    @Query("SELECT * FROM Restaurant")
    fun getAllBookMarkRestro(): MutableList<RestaurantEntity>?
}