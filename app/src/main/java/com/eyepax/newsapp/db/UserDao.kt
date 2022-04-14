package com.eyepax.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eyepax.newsapp.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUser(username: String, password: String): LiveData<List<User>>

}