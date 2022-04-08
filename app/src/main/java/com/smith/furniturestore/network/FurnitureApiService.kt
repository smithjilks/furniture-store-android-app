package com.smith.furniturestore.network

import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val  BASE_URL =
    "https://2d4e-197-232-61-236.ngrok.io/api/v1/"


/**
 * Build the Moshi object that Retrofit will be using,
 * Add the Kotlin adapter for full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the methods for network calls
 */
interface FurnitureApiService {

    // User Methods
    // Auth
    @POST("users/login")
    suspend fun authUser(@Body userAuthCredentials: UserAuthCredentials): UserInfo

    // Register User
    @POST("users/register")
    suspend fun registerUser(@Body userRegInfo: UserRegistrationInfo): ApiResponse



    // Catalog Methods
    @GET("catalog")
    suspend fun getCatalogItems(): List<CatalogItem>

    @POST("catalog")
    suspend fun createCatalogItem(@Body catalogItem: CatalogItem): ApiResponse


    // Orders
    @GET("orders/user/{id}")
    suspend fun getUserOrders(@Path("id") userId: String): List<OrderItem>

    @POST("orders")
    suspend fun createOrderItem(@Body orderItem: OrderItem): ApiResponse

}


/**
 * A public API object that exposes the lazy-initialized Retrofit service
 */

object FurnitureApi {
    val retrofitService: FurnitureApiService by lazy {
        retrofit.create(FurnitureApiService::class.java)
    }
}