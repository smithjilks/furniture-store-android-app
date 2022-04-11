package com.smith.furniturestore.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smith.furniturestore.data.database.dao.FurnitureDao
import com.smith.furniturestore.data.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CatalogItem::class, CartItem::class, OrderItem::class, UserInfo::class], version = 1, exportSchema = false)
@TypeConverters(CatalogItemConverter::class, CartItemConverter::class)
abstract class FurnitureDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao



    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: FurnitureDatabase? = null
        fun getDatabase(context: Context, coroutineScope: CoroutineScope): FurnitureDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FurnitureDatabase::class.java,
                    "catalog_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            coroutineScope.launch {
                                INSTANCE?.let {
                                    insertData(context, it.furnitureDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance

            }
        }

        suspend fun insertData(context: Context, furnitureDao: FurnitureDao) {
            furnitureDao.deleteAllCatalogItems()
            context.assets.open("catalog2.json").bufferedReader().use { inputStream ->
                val catalogItem = object : TypeToken<List<CatalogItem>>() {}.type
                val catalogItemsList: List<CatalogItem> =
                    Gson().fromJson(inputStream.readText(), catalogItem)
                Log.d("seed", "$catalogItemsList")
                furnitureDao.insertAllCatalogItems(catalogItemsList)
            }
        }

    }
}