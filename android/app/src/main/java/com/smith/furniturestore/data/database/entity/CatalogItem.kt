package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json

@Entity(tableName = "catalog_table")
data class CatalogItem(
    @PrimaryKey()
    @Json(name ="_id") val id: String,
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String
)

class CatalogItemConverter {
    @TypeConverter
    fun toCatalogItemList(value: String): List<CatalogItem> {
        val listType = object: TypeToken<List<CatalogItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toCatalogItemListJson(list: List<CatalogItem>): String {
        val type = object : TypeToken<List<CatalogItem>>() {}.type
        return Gson().toJson(list, type)
    }
}
