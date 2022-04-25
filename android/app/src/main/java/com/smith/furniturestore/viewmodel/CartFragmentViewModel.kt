package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CartFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // TotalCost observables
    private val _totalCost = MutableLiveData<Long>()
    val totalCost: LiveData<Long> = _totalCost

    // CachedIn makes sure even with config changes the data survives (or remains the same)
    // Tying it to view model scope to take advantage of view model lifecycle
    val getAllCartItemsAsPagingSource = furnitureRepository.getAllCartItems

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cartItem: CartItem) = viewModelScope.launch {
        furnitureRepository.insertCartItem(cartItem)
    }

    fun updateSingleCartItem(cartItem: CartItem) = viewModelScope.launch {
        furnitureRepository.updateSingleCartItem(cartItem)
    }

    fun deleteCartItem(id: String) = viewModelScope.launch {
        furnitureRepository.deleteSingleCartItem(id)
    }

    fun clearCart() {
        viewModelScope.launch {
            furnitureRepository.deleteAllCartItems()
            _totalCost.value = 0L
        }
    }

    fun updateCartTotalAmount(total: Long) {
        viewModelScope.launch {
            Log.d("Total cost in View Model", "Here is the total before sum ${_totalCost.value}")
            _totalCost.value = _totalCost.value?.plus(total)
            Log.d("Total cost in View Model", "Here is the total after sum " + _totalCost.value)
        }
    }

    fun initializeTotalAmount() {
        viewModelScope.launch {
            val allCartItems = furnitureRepository.getAllCartItemsAsList()
            var total = 0L
            for (cartItem in allCartItems) {
                total += cartItem.subTotal
            }
            _totalCost.value = total
        }
    }




}

class CartFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}
