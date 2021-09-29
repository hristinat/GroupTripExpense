package com.example.android.grouptripexpense.addMembers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.grouptripexpense.database.AppDatabase

class AddMembersViewModelFactory (
        private val dataSource: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMembersViewModel::class.java)) {
            return AddMembersViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}