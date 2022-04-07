package com.smith.furniturestore.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.smith.furniturestore.data.database.dao.CatalogDao
import com.smith.furniturestore.data.database.entity.CatalogItem

import kotlin.Suppress;

/**
 * Declares the DAO as a private property in the constructor. Pass in the DAO
 * instead of the whole database, because you only need access to the DAO
 */
class CatalogRepository(private val catalogDao: CatalogDao) {

    /**
     * Room executes all queries on a separate thread.
     * Observed Flow will notify the observer when the data has changed.
     */
  //  val allCatalogItemsFlow: Flow<List<CatalogItem>> = catalogDao.getCatalogItems()

    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCatalogItem(catalogItem: CatalogItem) {
        catalogDao.insert(catalogItem)
    }

    val getAllCatalogItems = Pager(PagingConfig(pageSize = 5, enablePlaceholders = true),
        pagingSourceFactory = { catalogDao.getAll() }
    ).flow

}