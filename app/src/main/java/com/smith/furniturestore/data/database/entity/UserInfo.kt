package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserInfo(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Int,
    val token: String,
    val userType: String,
)
