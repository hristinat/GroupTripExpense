package com.example.android.grouptripexpense.expenseTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.grouptripexpense.database.AppDatabase

class ExpenseTabViewModelFactory(
        private val dataSource: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseTabViewModel::class.java)) {
            return ExpenseTabViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}