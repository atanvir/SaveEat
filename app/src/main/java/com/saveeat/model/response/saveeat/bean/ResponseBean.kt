package com.saveeat.model.response.saveeat.bean
import android.os.Parcelable
import androidx.room.TypeConverters
import com.saveeat.utils.converter.ListConverter
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResponseBean(
    var results_found: Long,
    var results_start: Long,
    var results_shown: Long,
    @TypeConverters(ListConverter::class)
    var restaurants: MutableList<ResturantResponse>) : Parcelable{

}