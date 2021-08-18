package com.saveeat.model.response.SaveEat.bean

import android.os.Parcelable
import androidx.room.TypeConverters
import com.saveeat.repository.local.RestaurantEntity
import com.saveeat.utils.converter.ModelConverter
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResturantResponse (
    @TypeConverters(ModelConverter::class)
    var restaurant: RestaurantEntity
) : Parcelable