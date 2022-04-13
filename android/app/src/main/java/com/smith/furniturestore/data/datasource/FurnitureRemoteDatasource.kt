package com.smith.furniturestore.data.datasource

import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.smith.furniturestore.network.FurnitureApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FurnitureRemoteDatasource(
    private val furnitureAPI: FurnitureApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * All the executions are carried out on an IO-optimized thread
     * since the ApiService doesn't support coroutines and makes synchronous requests.
     */

    //User Methods
    suspend fun authenticateUser(userAuthCredentials: UserAuthCredentials): UserInfo =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.authUser(userAuthCredentials)
        }

    suspend fun createNewUser(userRegInfo: UserRegistrationInfo): ApiResponse =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.registerUser(userRegInfo)
        }


    // Catalog Methods
    suspend fun fetchCatalogItems(): List<CatalogItem> =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.getCatalogItems()
        }

    suspend fun createNewCatalogItem(
        token: String,
        title: RequestBody,
        shortDescription: RequestBody,
        longDescription: RequestBody,
        price: RequestBody,
        itemImage: MultipartBody.Part
    ): ApiResponse =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.createCatalogItem(
                token,
                title,
                shortDescription,
                longDescription,
                price,
                itemImage)
        }


    //Orders Methods
    suspend fun fetchAllOrders(): List<OrderItem> =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.getAllOrders()
        }

    suspend fun fetchUserOrders(userId: String): List<OrderItem> =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.getUserOrders(userId)
        }

    suspend fun createNewOrderItem(token:String, orderItem: OrderItem): ApiResponse =
        withContext(ioDispatcher) {
            furnitureAPI.retrofitService.createOrderItem(token, orderItem)
        }
}