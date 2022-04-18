package com.smith.furniturestore.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.viewmodel.SharedAuthViewModel
import com.smith.furniturestore.worker.LogoutWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivityViewModel(
    private val furnitureRepository: FurnitureRepository,
    application: Application
) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    internal val outputWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData("tokenWorker")




    init {
        // livedata in order to observe the changes
        startLogoutWorker()

    }

    private fun startLogoutWorker() {
        // Ensure unique work
        val work = PeriodicWorkRequestBuilder<LogoutWorker>(15, TimeUnit.MINUTES)
            .addTag("tokenWorker")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "tokenWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )

    }

    fun logoutUser() {
        viewModelScope.launch {
            furnitureRepository.deleteUserInfo()
            workManager.cancelUniqueWork("tokenWorker")
        }
    }

}

class MainActivityViewModelFactory(
    private val furnitureRepository: FurnitureRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(furnitureRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}