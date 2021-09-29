package com.example.android.grouptripexpense.createTrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.grouptripexpense.database.AppDatabase

class CreateTripViewModelFactory (
        private val dataSource: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTripViewModel::class.java)) {
            return CreateTripViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}