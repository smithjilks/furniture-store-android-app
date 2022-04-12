package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CheckoutFragmentViewModel(private val furnitureRepository: FurnitureRepository) :
    ViewModel() {

    // Payment observables
    private val _orderStatus = MutableLiveData<String>()
    val orderStatus: LiveData<String> = _orderStatus

    // Total Amount observables
    private val _totalAmount = MutableLiveData<Long>()
    val totalAmount: LiveData<Long> = _totalAmount

    // All Cart Items
    private val _allCartItems = MutableLiveData<List<CartItem>>()
    val allCartItems: LiveData<List<CartItem>> = _allCartItems

    // UserInfo observables
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    init {
        getSavedUserInfo()
        calculateTotalCostOfItems()
        getALlCartItemsFromCart()
    }


    private fun clearCart() {
        viewModelScope.launch {
            furnitureRepository.deleteAllCartItems()
        }
    }

    private fun getALlCartItemsFromCart() {
        viewModelScope.launch {
            _allCartItems.value = furnitureRepository.getAllCartItemsAsList()

        }
    }


    private fun calculateTotalCostOfItems() {
        viewModelScope.launch {
            var total = 0L
            val getAllItems = furnitureRepository.getAllCartItemsAsList()
            for (item in getAllItems) {
                total += item.subTotal
            }
            _totalAmount.value = total
        }
    }

    fun submitOrderToRemote(orderItem: OrderItem) {
        viewModelScope.launch {
            try {
                val apiResponse = furnitureRepository.createOrderItem(
                    "Bearer ${userInfo.value?.token}",
                    orderItem
                )
                clearCart()
                _orderStatus.value = "success"
            } catch (e: Exception) {
                e.printStackTrace()
                _orderStatus.value = "failed"
            }
        }
    }

    private fun getSavedUserInfo() {
        viewModelScope.launch {
            _userInfo.value = furnitureRepository.getUserInfo()
            Log.d("User Profile Info", _userInfo.toString())
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