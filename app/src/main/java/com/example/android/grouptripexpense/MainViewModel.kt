package com.example.android.grouptripexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.MemberEntry
import com.example.android.grouptripexpense.database.TripEntry

// TODO: pass db not application
class MainViewModel(application: Application) : AndroidViewModel(application) {
    var expenses: LiveData<List<ExpenseEntry>>
    var members: LiveData<List<MemberEntry>>
    var trips: LiveData<List<TripEntry>>

    init {
        val database = AppDatabase.getInstance(getApplication())
        expenses = database.expenseDao.loadAllExpensesLivedata()
        members = database.memberDao.loadAllMembersLiveData()
        trips = database.tripDao.loadAllTripsLiveData()
    }
}