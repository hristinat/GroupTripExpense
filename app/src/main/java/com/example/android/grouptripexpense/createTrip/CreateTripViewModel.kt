package com.example.android.grouptripexpense.createTrip

import androidx.lifecycle.*
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.TripEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateTripViewModel(private val database: AppDatabase) : ViewModel() {
    //TODO: check if it's the best approach
    val tripName = MutableLiveData<String>()
    val membersCount = MutableLiveData<String>()

    //TODO: Understand mediatorlivedata?
    val isTripNameValid = MediatorLiveData<Boolean>().apply {
        addSource(tripName) {
            value = it.isNotEmpty() && it.isNotBlank()
        }
    }

    val isMembersCountValid = MediatorLiveData<Boolean>().apply {
        addSource(membersCount) {
            value = it.isNotEmpty() && it.isNotBlank()
        }
    }

    private val _navigateToAddMembers = MutableLiveData<Boolean>()
    val navigateToAddMembers: LiveData<Boolean>
        get() = _navigateToAddMembers

    private val _navigateToExpenses = MutableLiveData<Boolean>()
    val navigateToExpenses: LiveData<Boolean>
        get() = _navigateToExpenses

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allTrips = database.tripDao.loadAllTrips()
                if (allTrips.size == 1 && database.memberDao.loadAllMembers().isEmpty()) {
                    _navigateToAddMembers.postValue(true)
                } else {
                    if (allTrips.size == 1) {
                        _navigateToExpenses.postValue(true)
                    }
                }
            }
        }
    }

    fun onNavigatedToAddMembers() {
        _navigateToAddMembers.value = false
    }

    fun onNavigateToExpenses() {
        _navigateToExpenses.value = false
    }

    fun onCreateButtonClicked() {
        viewModelScope.launch {
            if (tripName.value != null && membersCount.value != null) {
                val tripEntry = TripEntry(tripName.value!!, membersCount.value!!.toInt())
                insertTrip(tripEntry)
            }
        }
        _navigateToAddMembers.value = true
    }

    private suspend fun insertTrip(tripEntry: TripEntry) {
        withContext(Dispatchers.IO) {
            database.tripDao.insertTrip(tripEntry)
        }
    }
}