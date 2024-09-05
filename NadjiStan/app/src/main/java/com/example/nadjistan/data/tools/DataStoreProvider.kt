package com.example.nadjistan.data.tools

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.nadjistan.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

class DataStoreProvider(
 val context : Context
) {

   var n_allowed : Boolean? = false

 suspend fun isNotificationAllowed( set : (Boolean?) -> Unit){
  val NALLOWED = booleanPreferencesKey("Notification_allowed")
  Log.d("DATA_STORE_PROVIDER", "Starting flow")
  val flow: Flow<Boolean?> = context.dataStore.data
   .map { preferences ->
    preferences[NALLOWED]

   }
  Log.d("DATA_STORE_PROVIDER", "Flow collected")
  flow.collect { tmp ->
   Log.d("DATA_STORE_PROVIDER", "setting value")
   set(tmp)
  }
 }

 suspend fun setNotificationAllowed(f : Boolean){
  val NALLOWED = booleanPreferencesKey("Notification_allowed")
   context.dataStore.edit { settings ->
    settings[NALLOWED] = f
   }
 }

 suspend fun saveUUID(context: Context, uuid: UUID) {
  val UUID_KEY = stringPreferencesKey("uuid_key")
  context.dataStore.edit { preferences ->
   preferences[UUID_KEY] = uuid.toString()
  }
  Log.d("DATA_STORE_PROVIDER", uuid.toString())
 }

 suspend fun getUUID(context: Context, execute : (UUID) -> Unit) {
  val UUID_KEY = stringPreferencesKey("uuid_key")
  val preferences = context.dataStore.data
   .map { preferences ->
    preferences[UUID_KEY]
   }
   .first() // Collect the first value
  execute(preferences?.let { UUID.fromString(it) }!!)
 }

 suspend fun saveDRSF( f : Boolean) {
  val KEY = booleanPreferencesKey("drsf")
  context.dataStore.edit { preferences ->
   preferences[KEY] = f
  }
  Log.d("DATA_STORE_PROVIDER", "drsf: $f")
 }

 suspend fun getDRSF(execute : (Boolean?) -> Unit) {
  val KEY = booleanPreferencesKey("drsf")
  val preferences = context.dataStore.data
   .map { preferences ->
    preferences[KEY]
   }
   .first() // Collect the first value
  Log.d("DATA_STORE_PROVIDER", "drsf: $preferences")
  execute(preferences)
  Log.d("DATA_STORE_PROVIDER", "after execute")
 }
}