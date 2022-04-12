package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class CatalogItemDetailsFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {
    // User Profile Info observables
    private val _userProfileInfo = MutableLiveData<UserInfo>()
    val userProfileInfo: LiveData<UserInfo> = _userProfileInfo



    init {
        getSavedUserInfo()
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cartItem: CartItem) = viewModelScope.launch {
        furnitureRepository.insertCartItem(cartItem)
    }



    suspend fun getSingleCatalogItemById(id: String): CatalogItem {
            return  furnitureRepository.getCatalogItemById(id)
    }

    private fun getSavedUserInfo() {
        viewModelScope.launch {
            _userProfileInfo.value = furnitureRepository.getUserInfo()
            Log.d("User Profile Info", _userProfileInfo.toString())
        }
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