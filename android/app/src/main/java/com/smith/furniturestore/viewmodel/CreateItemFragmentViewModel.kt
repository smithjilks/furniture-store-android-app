package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.model.ApiResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateItemFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // Status observables
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status


    // UserInfo observables
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    init {
        getSavedUserInfo()
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun createNewItem(title: RequestBody,
                      shortDescription: RequestBody,
                      longDescription: RequestBody,
                      price: RequestBody,
                      itemImage: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                Log.d("API PAYLOAD", "${userInfo.value?.token} $title $shortDescription $longDescription $price $itemImage")
                val apiResponse: ApiResponse = furnitureRepository.createCatalogItem(
                    "Bearer ${userInfo.value?.token}",
                    title,
                    shortDescription,
                    longDescription,
                    price,
                    itemImage
                )
                _status.value = "success"
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = "failed"
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

class CreateItemFragmentViewModelFactory(private val furnitureRepository: FurnitureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateItemFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateItemFragmentViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}