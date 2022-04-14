package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UpdateStatusPayload
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

class OrderFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // User Profile Info observables
    private val _userProfileInfo = MutableLiveData<UserInfo>()
    val userProfileInfo: LiveData<UserInfo> = _userProfileInfo

    // Update Order status observables
    private val _statusLiveData = MutableLiveData<String>()
    val statusLiveData: LiveData<String> = _statusLiveData


    init {
        getSavedUserInfoAndSeedDb()
    }


    // CachedIn makes sure even with config changes the data survives (or remains the same)
    // Tying it to view model scope to take advantage of view model lifecycle
    val dbOrderItems = furnitureRepository.getAllOrderItems.cachedIn(viewModelScope)


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(catalogItem: CatalogItem) = viewModelScope.launch {
        furnitureRepository.insertCatalogItem(catalogItem)
    }

    private fun seedRoomDb() {
        viewModelScope.launch {
            try {
                if (_userProfileInfo.value?.userType == "admin") {
                    val orderItems: List<OrderItem> = furnitureRepository.fetchAllOrders()
                    furnitureRepository.insertAllOrderItems(orderItems)
                } else {
                    val orderItems: List<OrderItem> =
                        furnitureRepository.fetchUserOrders(_userProfileInfo.value?.id!!)
                    furnitureRepository.insertAllOrderItems(orderItems)
                }

            } catch (e: Exception) {
                Log.e("Fetch Order Items Error", e.toString())
            }

        }

    }

    private fun getSavedUserInfoAndSeedDb() {
        viewModelScope.launch {
            _userProfileInfo.value = furnitureRepository.getUserInfo()
            Log.d("User Profile Info", _userProfileInfo.toString())
            seedRoomDb()
        }
    }

    fun getUserType() : String? {
        return _userProfileInfo.value?.userType
    }

    fun updateOrderStatus(orderId: String, status: String) {
        val statusPayload = UpdateStatusPayload(
            status
        )
        val token = "Bearer ${_userProfileInfo.value?.token}"

        viewModelScope.launch {
            try {
                val response = furnitureRepository.updateOrderItem(token, orderId, statusPayload)
                val orderItem: OrderItem = furnitureRepository.fetchOrderById(orderId)
                furnitureRepository.insertOrderItem(orderItem)

                _statusLiveData.value = "success"

            } catch (e: Exception) {
                e.printStackTrace()
                _statusLiveData.value = "failed"

            }
        }
    }


}

class OrderFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}