package com.smith.furniturestore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.data.repository.FurnitureRepository
import kotlinx.coroutines.launch

class OrderFragmentViewModel(private val furnitureRepository: FurnitureRepository) : ViewModel() {

    // TotalCost observables






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