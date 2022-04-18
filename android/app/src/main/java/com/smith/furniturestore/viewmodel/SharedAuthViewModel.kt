package com.smith.furniturestore.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.work.*
import com.smith.furniturestore.app.AppScope.context
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.model.ApiResponse
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.smith.furniturestore.ui.AuthActivity
import com.smith.furniturestore.worker.LogoutWorker
import com.squareup.moshi.Json
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

class SharedAuthViewModel(
    private val furnitureRepository: FurnitureRepository,
    application: Application
) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)
    private val sharedPrefFile = "com.smith.furniturestore.user"
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)

    // Login observables
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    // Register observables
    private val _signupStatus = MutableLiveData<String>()
    val signupStatus: LiveData<String> = _signupStatus


    // User Profile Info observables
    private val _userProfileInfo = MutableLiveData<UserInfo>()
    var userProfileInfo: LiveData<UserInfo> = _userProfileInfo

    init {
        autoAuthUser()
    }


    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    private fun autoAuthUser() {
        viewModelScope.launch {
            val tokenExpiry =
                sharedPreferences.getString(AuthActivity.TOKEN_EXPIRY, "2022-04-17T14:07:41.728834")
            val tokenExpiryTimeStamp = LocalDateTime.parse(tokenExpiry)

            if (LocalDateTime.now().isAfter(tokenExpiryTimeStamp)){
                logoutUser()
                _loginStatus.value = "failed"
            } else {
                if (furnitureRepository.getUserInfo() != null) {
                    getSavedUserInfo()
                    _loginStatus.value = "success"
                }
            }
        }
    }


    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    fun loginUser(userAuthCredentials: UserAuthCredentials, application: Application) {
        viewModelScope.launch {
            try {
                val userInfo: UserInfo = furnitureRepository.authenticateUser(userAuthCredentials)
                furnitureRepository.insertUserInfo(userInfo)
                startLogoutWorker()
                saveTokenExpiryTimestamp()
                _loginStatus.value = "success"
            } catch (e: Exception) {
                Log.e("Auth Error", e.toString())
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
                val apiResponse: ApiResponse =
                    furnitureRepository.registerUser(userRegistrationInfo)
                _signupStatus.value = "success"
            } catch (e: Exception) {
                _signupStatus.value = "failed"
            }

        }
    }



    private fun startLogoutWorker() {
        // Ensure unique work
        val work = PeriodicWorkRequestBuilder<LogoutWorker>(15, TimeUnit.MINUTES)
            .addTag("tokenWorker")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "periodicTokenWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )

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

    /**
     * Launching a new coroutine to submit user registration details to api
     * in a non-blocking way
     */
    private fun getSavedUserInfo() {
        viewModelScope.launch {
            _userProfileInfo.value = furnitureRepository.getUserInfo()
            Log.d("User Profile Info", _userProfileInfo.toString())
        }
    }


    fun logoutUser() {
        viewModelScope.launch {
            furnitureRepository.deleteUserInfo()
            workManager.cancelUniqueWork("tokenWorker")
        }
    }

    private fun saveTokenExpiryTimestamp() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString(AuthActivity.TOKEN_EXPIRY, LocalDateTime.now().plusHours(1L).toString())
        editor.apply()
    }


}

class SharedAuthViewModelFactory(
    private val furnitureRepository: FurnitureRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedAuthViewModel(furnitureRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}