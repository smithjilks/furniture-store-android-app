package com.smith.furniturestore.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.smith.furniturestore.data.database.dao.FurnitureDao
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem

import kotlin.Suppress;

/**
 * Declares the DAO as a private property in the constructor. Pass in the DAO
 * instead of the whole database, because you only need access to the DAO
 */
class FurnitureRepository(private val furnitureDao: FurnitureDao) {

    /**
     * Room executes all queries on a separate thread.
     * Observed Flow will notify the observer when the data has changed.
     */


    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    // Catalog
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCatalogItem(catalogItem: CatalogItem) {
        furnitureDao.insertSingleCatalogItem(catalogItem)
    }

    val getAllCatalogItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { furnitureDao.getAllCatalogItems() }
    ).flow

    suspend  fun getCatalogItemById(id: String) : CatalogItem {
        return furnitureDao.getCatalogItemById(id)
    }


    //Cart
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCartItem(cartItem: CartItem) {
        furnitureDao.insertSingleCartItem(cartItem)
    }

    val getAllCartItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { furnitureDao.getAllCartItems() }
    ).flow

    suspend fun getCartItemById(id: String) : CartItem {
        return furnitureDao.getCartItemById(id)
    }

    suspend fun updateSingleCartItem(cartItem: CartItem) {
        return furnitureDao.updateSingleCartItem(cartItem)
    }

    suspend fun deleteSingleCartItem(id: String) {
        return furnitureDao.deleteSingleCartItem(id)
    }
}