package com.smith.furniturestore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CatalogItemDetailsFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {
    // CachedIn makes sure even with config changes the data survives (or remains the same)
    // Tying it to view model scope to take advantage of view model lifecycle


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cartItem: CartItem) = viewModelScope.launch {
        furnitureRepository.insertCartItem(cartItem)
    }



    suspend fun getSingleCatalogItemById(id: String): CatalogItem {
            return  furnitureRepository.getCatalogItemById(id)
    }


}

class CatalogItemDetailsFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CatalogItemDetailsFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  CatalogItemDetailsFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}