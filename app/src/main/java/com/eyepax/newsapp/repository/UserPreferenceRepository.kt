package com.eyepax.newsapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.eyepax.newsapp.AppConstant
import com.eyepax.newsapp.AppConstant.Companion.QUERY_PAGE_SIZE
import com.eyepax.newsapp.UserPreferences
import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.model.User
import com.eyepax.newsapp.network.NewsApiService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = AppConstant.USER_DATASTORE)

    suspend fun saveToDataStore(user: User) {
        context.dataStore.edit {

            it[UserPreferences.FIRSTNAME] = user.firstName ?: ""
            it[UserPreferences.LASTNAME] = user.lastName ?: ""
            it[UserPreferences.EMAIL] = user.emailAddress?:""
            it[UserPreferences.USERNAME] = user.username?:""
            it[UserPreferences.AUTHKEY] = user.id.toString()

        }
    }

    suspend fun getFromDataStore() = context.dataStore.data.map {
        User(
            firstName = it[UserPreferences.FIRSTNAME]?:"",
            lastName = it[UserPreferences.LASTNAME]?:"",
            emailAddress = it[UserPreferences.EMAIL]?:"",
            username = it[UserPreferences.USERNAME]?:"",
            id = it[UserPreferences.AUTHKEY]?.toInt()
        )
    }

    suspend fun clearDataStore(user: User) {
        context.dataStore.edit {

            it.clear()
        }
    }

}