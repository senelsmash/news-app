package com.eyepax.newsapp.repository

import com.eyepax.newsapp.db.NewsAppDatabase
import com.eyepax.newsapp.model.User
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val db: NewsAppDatabase
) {

    suspend fun insert(user: User) =
        db.getUserDao().insert(user)

    fun getUser(username: String, password: String) =
        db.getUserDao().getUser(username, password)
}