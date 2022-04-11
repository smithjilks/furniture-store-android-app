package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey
    val id: String,
    val title: String,
    val quantity: Int,
    val subTotal: Int,
    val imageUrl: String,
)

class CartItemConverter {
    @TypeConverter
    fun toCartItemItemList(value: String): List<CartItem> {
        val listType = object: TypeToken<List<CartItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toCatalogItemListJson(list: List<CartItem>): String {
        val type = object : TypeToken<List<CartItem>>() {}.type
        return Gson().toJson(list, type)
    }
}


