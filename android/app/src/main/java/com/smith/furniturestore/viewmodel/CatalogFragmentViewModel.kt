package com.smith.furniturestore.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CatalogFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {


    init {
        seedRoomDb()
    }


    // CachedIn makes sure even with config changes the data survives (or remains the same)
    // Tying it to view model scope to take advantage of view model lifecycle
    val getAll = furnitureRepository.getAllCatalogItems.cachedIn(viewModelScope)


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(catalogItem: CatalogItem) = viewModelScope.launch {
        furnitureRepository.insertCatalogItem(catalogItem)
    }

    private fun seedRoomDb() {
        viewModelScope.launch {
            try {
                val catalogItems: List<CatalogItem> = furnitureRepository.fetchCatalogItems()
                Log.d("Catalog Items", catalogItems.toString())
                furnitureRepository.insertAllCatalogItems(catalogItems)
            } catch (e: Exception) {
                Log.e("Fetch Catalog Error", e.toString())
            }

        }

    }

}

class CatalogFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CatalogFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  CatalogFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}