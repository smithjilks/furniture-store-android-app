package com.smith.furniturestore.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smith.furniturestore.data.database.entity.CatalogItem

@Dao
interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(catalogItems: List<CatalogItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(catalogItem: CatalogItem)

    @Query("SELECT * FROM catalog_table")
    fun getAll(): PagingSource<Int, CatalogItem>
    //PagingSource returns flow by default

    @Query("DELETE FROM catalog_table")
    suspend fun deleteAll()


}