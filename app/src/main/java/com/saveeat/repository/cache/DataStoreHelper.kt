package com.saveeat.repository.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.saveeat.repository.cache.PreferenceKeyConstants._id
import com.saveeat.repository.cache.PreferenceKeyConstants.name

class DataStoreHelper(var dataStore: DataStore<Preferences>) {

    suspend fun storeUserData(id: Int?,_name : String?) {
        dataStore?.edit { prefs ->
            prefs[_id] = id ?: 0
            prefs[name] = _name ?: ""
        }
    }
}