package com.smith.furniturestore.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cartItems: List<CartItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_table")
    fun getAll(): PagingSource<Int, CartItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM cart_table")
    fun getAllAsList(): List<CartItem>

    @Query("DELETE FROM cart_table")
    suspend fun deleteAll()


}