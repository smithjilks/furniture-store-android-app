package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class OrderFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // User Profile Info observables
    private val _userProfileInfo = MutableLiveData<UserInfo>()
    val userProfileInfo: LiveData<UserInfo> = _userProfileInfo


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