package com.smith.furniturestore

import androidx.test.platform.app.InstrumentationRegistry
import com.smith.furniturestore.network.FurnitureApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStreamReader

class FurnitureApiServiceTests {

    private lateinit var service: FurnitureApiService

//    object FileReader {
//        fun readStringFromFile(fileName: String): String {
//            try {
//                val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
//                    .applicationContext as ).assets.open(fileName)
//                val builder = StringBuilder()
//                val reader = InputStreamReader(inputStream, "UTF-8")
//                reader.readLines().forEach {
//                    builder.append(it)
//                }
//                return builder.toString()
//            } catch (e: IOException) {
//                throw e
//            }
//        }
//    }

    @Before
    fun setup() {
        val mockWebServer = MockWebServer()
        val url = mockWebServer.url("/")

        service = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            ))
            .build()
            .create(FurnitureApiService::class.java)
    }

    @Test
    fun api_service() {

        runBlocking {
            val apiResponse = service.getCatalogItems()
            assertNotNull(apiResponse)
            assertTrue("The list was empty", apiResponse.isNotEmpty())
        }
    }

}