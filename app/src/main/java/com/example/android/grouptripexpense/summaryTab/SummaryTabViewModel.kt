package com.example.android.grouptripexpense.summaryTab

import androidx.lifecycle.*
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.MemberEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SummaryTabViewModel(private val database: AppDatabase) : ViewModel() {

    private val members = database.memberDao.loadAllMembersLiveData()
    private val expenses = database.expenseDao.loadAllExpensesLivedata()

    private val _navigateToCreateTrip = MutableLiveData<Boolean>()
    val navigateToCreateTrip: LiveData<Boolean>
        get() = _navigateToCreateTrip

    fun onNavigatedToCreateTrip() {
        _navigateToCreateTrip.value = false
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

    fun onFinishButtonClick() {
        viewModelScope.launch {
            deleteAll()
        }
        _navigateToCreateTrip.value = true
    }

    private suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            database.tripDao.deleteAllTrips()
            database.expenseDao.deleteAllExpenses()
            database.memberDao.deleteAllMembers()
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