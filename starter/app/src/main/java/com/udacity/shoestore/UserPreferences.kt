package com.udacity.shoestore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferences (
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )
    }

    val username: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USERNAME]
        }

    val email: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_EMAIL]
        }

    private val isUserLoggedIn: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_LOGGED_IN]
        }

    suspend fun saveCredentials(username: String, email: String){
        dataStore.edit { preferences ->
            preferences[KEY_EMAIL] = email
            preferences[KEY_USERNAME] = username
            preferences[KEY_USER_LOGGED_IN] = true
        }
    }

    suspend fun isUserLoggedIn(): Boolean = isUserLoggedIn.first() ?: false

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_USERNAME = preferencesKey<String>("key_username")
        private val KEY_EMAIL = preferencesKey<String>("key_email")
        private val KEY_USER_LOGGED_IN = preferencesKey<Boolean>("key_is_user_logged_in")
    }

}