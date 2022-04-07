package com.smith.furniturestore.app

import android.app.Application
import com.smith.furniturestore.data.database.FurnitureDatabase
import com.smith.furniturestore.data.repository.CartRepository
import com.smith.furniturestore.data.repository.CatalogRepository

class App : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

    private val db by lazy {
        FurnitureDatabase.getDatabase(this, AppScope)
    }

    val catalogRepository by lazy {
        CatalogRepository(db.catalogDao())
    }

    val cartRepository by lazy {
        CartRepository(db.cartDao())
    }

}