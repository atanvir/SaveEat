package com.saveeat.utils.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

 class ListConverter<T> {
    @TypeConverter
    fun toList(json: String): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(torrent: List<T>): String {
        val type = object: TypeToken<List<T>>() {}.type
        return Gson().toJson(torrent, type)
    }

}