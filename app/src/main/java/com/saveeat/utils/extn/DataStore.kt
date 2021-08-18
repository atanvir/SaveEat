package com.saveeat.utils.extn

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.io.IOException

suspend inline fun <reified T> DataStore<Preferences>.getData(PreferencesKey: Preferences.Key<T>, crossinline func: T.() -> Unit) {
    data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[PreferencesKey] }
        .collect { it?.let { func.invoke(it as T) } }
}