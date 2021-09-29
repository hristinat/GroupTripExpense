package com.example.android.grouptripexpense.addMembers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.MemberEntry
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMembersViewModel(private val database: AppDatabase) : ViewModel() {
    val firstMemberName = MutableLiveData<String>()
    var numberOfLines = 1
    var lastAddedEditText: TextInputLayout? = null

    var membersNames: MutableList<String> = mutableListOf()

    var trips = database.tripDao.loadAllTripsLiveData()

    private val _navigateToExpenses = MutableLiveData<Boolean>()
    val navigateToExpenses: LiveData<Boolean>
        get() = _navigateToExpenses

    private val _saveButtonClicked = MutableLiveData<Boolean>()
    val saveButtonClicked: LiveData<Boolean>
        get() = _saveButtonClicked

    private val _isMembersNameEmpty = MutableLiveData<Boolean>()
    val isMembersNameEmpty: LiveData<Boolean>
        get() = _isMembersNameEmpty

    private val _sameMembersNames = MutableLiveData<Boolean>()
    val sameMembersNames: LiveData<Boolean>
        get() = _sameMembersNames

    fun saveButtonClicked() {
        _saveButtonClicked.value = false
    }

    fun navigatedToExpenses() {
        _navigateToExpenses.value = false
    }

//    fun onAddButtonClicked() {
//        val memberName: String = if (numberOfLines == 1) {
//            firstMemberName.value.toString()
//        } else {
//            lastAddedEditText?.text.toString().trim { it <= ' ' }
//        }
//        _isMembersNameEmpty.value = memberName.isEmpty()
//        if (_isMembersNameEmpty.value == true) {
//            return
//        }
//
//        members.add(MemberEntry(memberName))
//        _addButtonClicked.value = true
//    }

    fun onSaveButtonClicked() {
//        _isMembersEmpty.value = members.size == 0
//        if (_isMembersEmpty.value == true) {
//            return
//        }
//
//        viewModelScope.launch {
//            insertMembers(members)
//        }
        _saveButtonClicked.value = true
    }

    fun saveMembers() {
        if (membersNames.size != trips.value?.get(0)?.membersCount) {
            _isMembersNameEmpty.value = true
            return
        }

        val uniqueNames = membersNames.toSet()
        if (membersNames.size != uniqueNames.size) {
            _sameMembersNames.value = true
            return
        }

        viewModelScope.launch {
            val members = membersNames.map { MemberEntry(it) }
            insertMembers(members)
            println()
        }

        _navigateToExpenses.value = true
    }

    private suspend fun insertMembers(members: List<MemberEntry>) {
        withContext(Dispatchers.IO) {
            database.memberDao.insertAllMembers(members)
            println()
        }
    }
}