package com.smith.furniturestore.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

class UserInfoConverter {
    @TypeConverter
    fun toUserInfoItemList(value: String): List<UserInfo> {
        val listType = object: TypeToken<List<UserInfo>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toUserInfoListJson(list: List<UserInfo>): String {
        val type = object : TypeToken<List<UserInfo>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toUserInfoJson(userInfo: UserInfo): String {
        val type = object : TypeToken<UserInfo>() {}.type
        return Gson().toJson(userInfo, type)
    }

    @TypeConverter
    fun toUserInfoItem(value: String): UserInfo {
        val objectType = object: TypeToken<UserInfo>() {}.type
        return Gson().fromJson(value, objectType)
    }



}
