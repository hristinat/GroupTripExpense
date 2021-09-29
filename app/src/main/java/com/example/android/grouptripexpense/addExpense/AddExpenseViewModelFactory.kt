package com.example.android.grouptripexpense.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.grouptripexpense.addMembers.AddMembersViewModel
import com.example.android.grouptripexpense.database.AppDatabase

class AddExpenseViewModelFactory (
        private val dataSource: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            return AddExpenseViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}