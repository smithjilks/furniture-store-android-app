package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "orders_table")
data class OrderItem(
    @PrimaryKey
    val id: Int,
    val date: String,
    val totalCost: Int,
    val items: List<CartItem>,
    val deliveryLat: String,
    val deliveryLong: String,
    val orderStatus: String
)