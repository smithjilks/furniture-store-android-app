package com.smith.furniturestore.network

import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UpdateStatusPayload
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL =
    "https://0e9e-197-232-61-234.ngrok.io/api/v1/"


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
    @POST("users/signup")
    suspend fun registerUser(@Body userRegInfo: UserRegistrationInfo): ApiResponse


    // Catalog Methods
    @GET("catalog")
    suspend fun getCatalogItems(): List<CatalogItem>

    @POST("catalog")
    @Multipart
    suspend fun createCatalogItem(
        @Header("authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("shortDescription") shortDescription: RequestBody,
        @Part("longDescription") longDescription: RequestBody,
        @Part("price") price: RequestBody,
        @Part itemImage: MultipartBody.Part
    ): ApiResponse


    // Orders
    @GET("orders")
    suspend fun getAllOrders(): List<OrderItem>

    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): OrderItem


    @GET("orders/user/{id}")
    suspend fun getUserOrders(@Path("id") userId: String): List<OrderItem>

    @POST("orders")
    suspend fun createOrderItem(
        @Header("authorization") token: String,
        @Body orderItem: OrderItem
    ): ApiResponse


    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Header("authorization") token: String,
        @Path("id") orderId: String,
        @Body status: UpdateStatusPayload
    ): ApiResponse

}


/**
 * A public API object that exposes the lazy-initialized Retrofit service
 */

object FurnitureApi {
    val retrofitService: FurnitureApiService by lazy {
        retrofit.create(FurnitureApiService::class.java)
    }
}
