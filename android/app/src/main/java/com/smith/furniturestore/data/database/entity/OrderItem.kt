package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json

@Entity(tableName = "orders_table")
data class OrderItem(
    @PrimaryKey
    @Json(name ="_id") val id: String,
    val totalCost: Int,
    val items: List<CartItem>,
    val creator: String,
    val deliveryLat: String,
    val deliveryLong: String,
    val orderStatus: String,
    val createdAt: String,
    val updatedAt: String
)