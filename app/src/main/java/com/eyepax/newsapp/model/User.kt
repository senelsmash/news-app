package com.eyepax.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var username: String? = "",
    var password: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var emailAddress: String? = ""
) : Serializable
