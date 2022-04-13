package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "orders_table")
data class OrderItem(
    @PrimaryKey
    @Json(name ="_id") val id: String,
    val totalCost: Long,
    val items: List<CartItem>,
    val creator: String,
    val deliveryLat: Double,
    val deliveryLong: Double,
    val orderStatus: String,
    val createdAt: String,
    val updatedAt: String,
    val userDetails: UserInfo
)