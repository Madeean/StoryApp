package com.madeean.storyapp2.ui.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class AuthenticationPreferences private constructor(private val dataStore: DataStore<Preferences>) {

  private val DATA_TOKEN = stringPreferencesKey("data_token")

  suspend fun saveDataToken(token: String) {
    dataStore.edit { preferences ->
      preferences[DATA_TOKEN] = token
    }
  }

  suspend fun clearToken() {
    dataStore.edit { preferences ->
      preferences[DATA_TOKEN] = ""
    }
  }

  fun getDataToken(): Flow<String> {
    return dataStore.data.map { preferences ->
      preferences[DATA_TOKEN] ?: ""
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: AuthenticationPreferences? = null

    fun getInstance(dataStore: DataStore<Preferences>): AuthenticationPreferences {
      return INSTANCE ?: synchronized(this) {
        val instance = AuthenticationPreferences(dataStore)
        INSTANCE = instance
        instance
      }
    }
  }

}