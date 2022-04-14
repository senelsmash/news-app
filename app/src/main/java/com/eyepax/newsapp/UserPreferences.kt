package com.eyepax.newsapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.eyepax.newsapp.AppConstant.Companion.USER_DATASTORE
import com.eyepax.newsapp.model.User
import kotlinx.coroutines.flow.map

class UserPreferences(
    val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = USER_DATASTORE)

    companion object {

        val FIRSTNAME = stringPreferencesKey("FNAME")
        val LASTNAME = stringPreferencesKey("LNAME")
        val EMAIL = stringPreferencesKey("EMAIL")
        val USERNAME = stringPreferencesKey("USERNAME")
        val AUTHKEY = stringPreferencesKey("AUTHKEY")

    }

    suspend fun saveToDataStore(user: User) {
        context.dataStore.edit {

            it[FIRSTNAME] = user.firstName ?: ""
            it[LASTNAME] = user.lastName ?: ""
            it[EMAIL] = user.emailAddress?:""
            it[USERNAME] = user.username?:""
            it[AUTHKEY] = user.id.toString()

        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        User(
            firstName = it[FIRSTNAME]?:"",
            lastName = it[LASTNAME]?:"",
            emailAddress = it[EMAIL]?:"",
            username = it[USERNAME]?:"",
            id = it[AUTHKEY]?.toInt()
        )
    }

}