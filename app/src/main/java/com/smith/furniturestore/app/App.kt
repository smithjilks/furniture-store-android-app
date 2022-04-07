package com.smith.furniturestore.app

import android.app.Application
import com.smith.furniturestore.data.database.FurnitureDatabase
import com.smith.furniturestore.data.repository.FurnitureRepository

class App : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

    private val db by lazy {
        FurnitureDatabase.getDatabase(this, AppScope)
    }

    val furnitureRepository by lazy {
        FurnitureRepository(db.furnitureDao())
    }


}