package com.smith.furniturestore.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.smith.furniturestore.data.database.dao.FurnitureDao
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.datasource.FurnitureRemoteDatasource
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Part

import kotlin.Suppress;

/**
 * Declares the DAO as a private property in the constructor. Pass in the DAO
 * instead of the whole database, because you only need access to the DAO
 */
class FurnitureRepository(
    private val furnitureDao: FurnitureDao,
    private val furnitureRemoteDatasource: FurnitureRemoteDatasource
) {


    // Room Database Data
    /**
     * Room executes all queries on a separate thread.
     * Observed Flow will notify the observer when the data has changed.
     */


    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */


    /**
     * Methods for accessing [Catalog Table]
     */

    //@WorkerThread
    suspend fun insertCatalogItem(catalogItem: CatalogItem) {
        furnitureDao.insertSingleCatalogItem(catalogItem)
    }

    suspend fun insertAllCatalogItems(catalogItems: List<CatalogItem>) {
        furnitureDao.insertAllCatalogItems(catalogItems)
    }

    val getAllCatalogItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { furnitureDao.getAllCatalogItems() }
    ).flow

    suspend fun getCatalogItemById(id: String): CatalogItem {
        return furnitureDao.getCatalogItemById(id)
    }


    /**
     * Methods for accessing [Cart Table]
     */

    suspend fun insertCartItem(cartItem: CartItem) {
        furnitureDao.insertSingleCartItem(cartItem)
    }

    val getAllCartItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { furnitureDao.getAllCartItems() }
    ).flow

    suspend fun getAllCartItemsAsList(): List<CartItem> {
        return furnitureDao.getAllCartItemsAsList()
    }

    suspend fun getCartItemById(id: String): CartItem {
        return furnitureDao.getCartItemById(id)
    }

    suspend fun updateSingleCartItem(cartItem: CartItem) {
        return furnitureDao.updateSingleCartItem(cartItem)
    }

    suspend fun deleteSingleCartItem(id: String) {
        return furnitureDao.deleteSingleCartItem(id)
    }

    suspend fun deleteAllCartItems() {
        furnitureDao.deleteAllCartItems()
    }


    /**
     * Methods for accessing [Order Table]
     */

    suspend fun insertOrderItem(orderItem: OrderItem) {
        furnitureDao.insertSingleOrderItem(orderItem)
    }

    suspend fun insertAllOrderItems(orderItems: List<OrderItem>) {
        furnitureDao.insertAllOrderItems(orderItems)
    }

    val getAllOrderItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { furnitureDao.getAllOrderItems() }
    ).flow

    suspend fun getOrderItemById(id: String): OrderItem {
        return furnitureDao.getOrderItemById(id)
    }

    suspend fun updateSingleOrderItem(orderItem: OrderItem) {
        return furnitureDao.updateSingleOrderItem(orderItem)
    }

    suspend fun deleteSingleOrderItem(id: String) {
        return furnitureDao.deleteSingleOrderItem(id)
    }

    suspend fun deleteAllOrderItems() {
        furnitureDao.deleteAllOrderItems()
    }


    /**
     * Methods for accessing [User Table]
     */

    suspend fun insertUserInfo(userInfo: UserInfo) {
        furnitureDao.insertUserInfo(userInfo)
    }

    suspend fun getUserInfo(): UserInfo {
        return furnitureDao.getUserInfo()
    }

    suspend fun getUserInfoById(id: String): UserInfo {
        return furnitureDao.getUserInfoById(id)
    }

    suspend fun updateUserInfo(userInfo: UserInfo) {
        return furnitureDao.updateUserInfo(userInfo)
    }

    suspend fun deleteUserInfo() {
        furnitureDao.deleteUserInfo()
    }


    /**
     * Methods for access remote datasource
     */

    /**
     * Methods for interacting with Remote API [User Data]
     */
    suspend fun authenticateUser(userAuthCredentials: UserAuthCredentials): UserInfo {
        return furnitureRemoteDatasource.authenticateUser(userAuthCredentials)
    }

    suspend fun registerUser(userRegistrationInfo: UserRegistrationInfo): ApiResponse {
        return furnitureRemoteDatasource.createNewUser(userRegistrationInfo)
    }


    /**
     * Methods for interacting with Remote API [Orders Data]
     */

    suspend fun fetchAllOrders(): List<OrderItem> {
        return furnitureRemoteDatasource.fetchAllOrders()
    }

    suspend fun fetchUserOrders(userId: String): List<OrderItem> {
        return furnitureRemoteDatasource.fetchUserOrders(userId)
    }

    suspend fun createOrderItem(token:String, orderItem: OrderItem) {
        furnitureRemoteDatasource.createNewOrderItem(token, orderItem)
    }


    /**
     * Methods for interacting with Remote API [Catalog Data]
     */

    suspend fun fetchCatalogItems(): List<CatalogItem> {
        return furnitureRemoteDatasource.fetchCatalogItems()
    }

    suspend fun createCatalogItem(
        token: String,
        title: RequestBody,
        shortDescription: RequestBody,
        longDescription: RequestBody,
        price: RequestBody,
        itemImage: MultipartBody.Part
    ): ApiResponse {
        return furnitureRemoteDatasource.createNewCatalogItem(
            token,
            title,
            shortDescription,
            longDescription,
            price,
            itemImage
        )
    }

}