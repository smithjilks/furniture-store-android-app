package com.smith.furniturestore.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem

@Dao
interface FurnitureDao {

    //Catalog Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCatalogItems(catalogItems: List<CatalogItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleCatalogItem(catalogItem: CatalogItem)

    @Query("SELECT * FROM catalog_table")
    fun getAllCatalogItems(): PagingSource<Int, CatalogItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM catalog_table where id=:id")
    suspend fun getCatalogItemById(id: String): CatalogItem

    @Query("DELETE FROM catalog_table")
    suspend fun deleteAllCatalogItems()

    @Query("DELETE FROM catalog_table where id=:id")
    suspend fun deleteSingleCatalogItem(id: String)


    // Cart Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCartItems(catalogItems: List<CatalogItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleCartItem(cartItem: CartItem)

    @Update
    suspend fun updateSingleCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): PagingSource<Int, CartItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM cart_table where id=:id")
    fun getCartItemById(id: String): CartItem

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCartItems()

    @Query("DELETE FROM cart_table where id=:id")
    suspend fun deleteSingleCartItem(id: String)


}