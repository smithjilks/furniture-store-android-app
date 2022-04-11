package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "user_table")
data class UserInfo(
    @PrimaryKey
    @Json(name ="_id") val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Long,
    val token: String,
    val userType: String,
    val createdAt: String,
    val updatedAt: String
)
