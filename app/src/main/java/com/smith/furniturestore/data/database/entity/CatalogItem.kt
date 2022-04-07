package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "catalog_table")
data class CatalogItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val price: Int,
    val imageUrl: String
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
