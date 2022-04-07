package com.smith.furniturestore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartFragmentViewModel(private val cartRepository: CartRepository) : ViewModel() {

    // CachedIn makes sure even with config changes the data survives (or remains the same)
    // Tying it to view model scope to take advantage of view model lifecycle
    val getAll = cartRepository.getAllCartItems.cachedIn(viewModelScope)


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cartItem: CartItem) = viewModelScope.launch {
        cartRepository.insertCartItem(cartItem)
    }
}

class CartFragmentViewModelFactory(private val cartRepository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  CartFragmentViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}