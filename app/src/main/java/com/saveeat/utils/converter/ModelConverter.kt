package com.saveeat.utils.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ModelConverter<T> {
    @TypeConverter
    fun toModel(json: String): T {
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(model: T) = Gson().toJson(model)
}