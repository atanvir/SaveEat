package com.saveeat.repository.local

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Restaurant")
data class RestaurantEntity  (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id:String,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "photos_url")
    var photos_url:String,

    @ColumnInfo(name = "average_cost_for_two")
    var average_cost_for_two:String,

    @ColumnInfo(name = "currency")
    var currency:String) : Parcelable {
}