package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CheckoutFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // Payment observables
    private val _paymentStatus = MutableLiveData<String>()
    val paymentStatus: LiveData<String> = _paymentStatus

    // Total Amount observables
    private val _totalAmount = MutableLiveData<Long>()
    val totalAmount: LiveData<Long> = _totalAmount


    init {
        calculateTotalCostOfItems()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun clearCart() {
        viewModelScope.launch {
            furnitureRepository.deleteAllCartItems()
        }
    }

    private fun calculateTotalCostOfItems() {
        viewModelScope.launch {
            var total = 0L
            val getAllItems = furnitureRepository.getAllCartItemsAsList()
            for(item in getAllItems) {
                total += item.subTotal
            }
            _totalAmount.value = total
        }
    }






}

class CheckoutFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}