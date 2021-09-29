package com.example.android.grouptripexpense.expenseTab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.MemberEntry

class ExpenseTabViewModel(database: AppDatabase): ViewModel() {

    private val members = database.memberDao.loadAllMembersLiveData()
    private val expenses = database.expenseDao.loadAllExpensesLivedata()

    private val _navigateToAddExpense = MutableLiveData<Boolean>()
    val navigateToAddExpense: LiveData<Boolean>
        get() = _navigateToAddExpense

    fun onNavigatedToAddExpense() {
        _navigateToAddExpense.value = false
    }

    fun onFabClick() {
        _navigateToAddExpense.value = true
    }

    val membersExpensesLiveData = MediatorLiveData<Pair<List<MemberEntry>?, List<ExpenseEntry>?>>()

    init {
        membersExpensesLiveData.addSource(members) {
            membersExpensesLiveData.value = combineLatestData(members, expenses)
        }

        membersExpensesLiveData.addSource(expenses) {
            membersExpensesLiveData.value = combineLatestData(members, expenses)
        }
    }

    private fun combineLatestData(
            membersLiveData: LiveData<List<MemberEntry>>,
            expensesLiveData: LiveData<List<ExpenseEntry>>
    ): Pair<List<MemberEntry>?, List<ExpenseEntry>?>? {

        val members = membersLiveData.value
        val expenses = expensesLiveData.value

        // Don't send a success until we have both results
        if (members == null || expenses == null) {
            return null
        }

        // TODO: Check for errors and return UserDataError if any.

        return Pair(members, expenses)
    }
}