package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey
    val id: Int,
    val title: String,
    val quantity: Int,
    val subTotal: Int,
    val imageUrl: String,
)

