package com.smith.furniturestore.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo

@Dao
interface FurnitureDao {

    /**
     * [Catalog table] Queries
     */
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


    /**
     * [Cart table] Queries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCartItems(catalogItems: List<CatalogItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleCartItem(cartItem: CartItem)

    @Update
    suspend fun updateSingleCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): PagingSource<Int, CartItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM cart_table")
    suspend fun getAllCartItemsAsList(): List<CartItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM cart_table where id=:id")
    fun getCartItemById(id: String): CartItem

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCartItems()

    @Query("DELETE FROM cart_table where id=:id")
    suspend fun deleteSingleCartItem(id: String)



    /**
     * [Orders table] Queries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrderItems(orderItems: List<OrderItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleOrderItem(orderItem: OrderItem)

    @Update
    suspend fun updateSingleOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM orders_table")
    fun getAllOrderItems(): PagingSource<Int, OrderItem>
    //PagingSource returns flow by default

    @Query("SELECT * FROM orders_table where id=:id")
    fun getOrderItemById(id: String): OrderItem

    @Query("DELETE FROM orders_table")
    suspend fun deleteAllOrderItems()

    @Query("DELETE FROM orders_table where id=:id")
    suspend fun deleteSingleOrderItem(id: String)


    /**
     * [User table] Queries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Update
    suspend fun updateUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM user_table")
    suspend fun getUserInfo(): UserInfo
    //PagingSource returns flow by default

    @Query("SELECT * FROM user_table where id=:id")
    fun getUserInfoById(id: String): UserInfo

    @Query("DELETE FROM user_table")
    suspend fun deleteUserInfo()



}