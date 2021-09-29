package com.example.android.grouptripexpense.summaryTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.expenseTab.ExpenseTabViewModel

class SummaryTabViewModelFactory (
        private val dataSource: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryTabViewModel::class.java)) {
            return SummaryTabViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}