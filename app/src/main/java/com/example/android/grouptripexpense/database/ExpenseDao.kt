package com.example.android.grouptripexpense.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense")
    fun loadAllExpensesLivedata(): LiveData<List<ExpenseEntry>>

    @Query("SELECT * FROM expense")
    fun loadAllExpenses(): List<ExpenseEntry>

    @Insert
    fun insertExpense(expenseEntry: ExpenseEntry)

    @Query("Delete FROM expense")
    fun deleteAllExpenses()
}