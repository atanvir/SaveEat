package com.saveeat.repository.cache

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeyConstants {
    const val CACHE_NAME="cache_data"

    val _id= intPreferencesKey("_id")
    val name= stringPreferencesKey("name")



}