package com.smith.furniturestore.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.smith.furniturestore.data.database.dao.CartDao
import com.smith.furniturestore.data.database.entity.CartItem

import kotlin.Suppress;

/**
 * Declares the DAO as a private property in the constructor. Pass in the DAO
 * instead of the whole database, because you only need access to the DAO
 */
class CartRepository(private val cartDao: CartDao) {

    /**
     * Room executes all queries on a separate thread.
     * Observed Flow will notify the observer when the data has changed.
     */
  //  val allCartItemsFlow: Flow<List<CartItem>> = cartDao.getCartItems()

    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
    }

    val getAllCartItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { cartDao.getAll() }
    ).flow

}