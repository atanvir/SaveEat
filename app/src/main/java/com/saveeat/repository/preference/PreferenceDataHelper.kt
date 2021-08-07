package com.saveeat.repository.preference

import android.content.Context
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import androidx.datastore.preferences.core.intPreferencesKey
import com.saveeat.repository.preference.PreferenceKeyConstants.ID

class PreferenceDataHelper(context: Context?) {

    private val dataStore = context?.openOrCreateDatabase("user_prefs",Context.MODE_PRIVATE,null)

    companion object {
      val key= intPreferencesKey(ID)
    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeData() {
//        dataStore.ed
//        dataStore.edit {
//            it[USER_AGE_KEY] = age
//            it[USER_NAME_KEY] = name
//            // here it refers to the preferences we are editing
//        }
    }

//    // Create an age flow to retrieve age from the preferences
//    // flow comes from the kotlin coroutine
//    val userAgeFlow: Flow<Int> = dataStore.data.map {
//        it[USER_AGE_KEY] ?: 0
//    }
//
//    // Create a name flow to retrieve name from the preferences
//    val userNameFlow: Flow<String> = dataStore.data.map {
//        it[USER_NAME_KEY] ?: ""
//    }
}