package com.smith.furniturestore.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.squareup.moshi.Json
import kotlinx.coroutines.launch
import java.lang.Exception

class SharedAuthViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {


    // Login observables
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    // Register observables
    private val _signupStatus = MutableLiveData<String>()
    val signupStatus: LiveData<String> = _signupStatus


    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    fun loginUser(userAuthCredentials: UserAuthCredentials) {
        viewModelScope.launch {
            try {
                val userInfo: UserInfo = furnitureRepository.authenticateUser(userAuthCredentials)
                furnitureRepository.insertUserInfo(userInfo)
                _loginStatus.value = "Success"
            } catch (e: Exception) {
                _loginStatus.value = "failed"
            }

        }
    }


    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    fun signupUser(userRegistrationInfo: UserRegistrationInfo) {
        viewModelScope.launch {
            try {
                val apiResponse: ApiResponse = furnitureRepository.registerUser(userRegistrationInfo)
                _signupStatus.value = "Success"
            } catch (e: Exception) {
                _signupStatus.value = "failed"
            }

        }
    }

    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    fun insert(userInfo: UserInfo) {
        viewModelScope.launch {
            try {
                furnitureRepository.insertUserInfo(userInfo)
            } catch (e: Exception) {

            }

        }
    }



}

class SharedAuthViewModelFactory(private val furnitureRepository: FurnitureRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  SharedAuthViewModel(furnitureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}