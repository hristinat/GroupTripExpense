package com.example.android.grouptripexpense.addExpense

import androidx.lifecycle.*
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.MemberEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddExpenseViewModel(private val database: AppDatabase) : ViewModel() {

    val whoPaidItemIdPosition = MutableLiveData<Long>()
    private val whoPaid
        get() =
            whoPaidItemIdPosition.value?.let { inputName ->
                members[inputName.toInt()]
            }

    val expenseType = MutableLiveData<String>()
    val amount = MutableLiveData<String>()

    val isExpenseTypeValid = MediatorLiveData<Boolean>().apply {
        addSource(expenseType) {
            value = it.isNotEmpty() && it.isNotBlank()
        }
    }

    val isAmountValid = MediatorLiveData<Boolean>().apply {
        addSource(amount) {
            value = it.isNotEmpty() && it.isNotBlank()
        }
    }

    private val _isWhoPaidIncorrect = MutableLiveData<Boolean>()
    val isWhoPaidIncorrect: LiveData<Boolean>
        get() = _isWhoPaidIncorrect

    private val _navigateToExpenses = MutableLiveData<Boolean>()
    val navigateToExpenses: MutableLiveData<Boolean>
        get() = _navigateToExpenses

    private val _membersNames = MutableLiveData<List<String>>()
    val membersNames: LiveData<List<String>>
        get() = _membersNames

    private var members = emptyList<MemberEntry>()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                members = database.memberDao.loadAllMembers()
                _membersNames.postValue(members.map { it.name })
            }
        }
    }

    fun onAddExpenseButtonClick() {
//        if (expenseType.isEmpty()) {
//            Toast.makeText(requireActivity(), "Please add expense type.", Toast.LENGTH_LONG).show()
//            return
//        }
//        if (amount.isEmpty()) {
//            Toast.makeText(requireActivity(), "Please add amount.", Toast.LENGTH_LONG).show()
//            return
//        }

//        if (whoPaid == null) {
//            _isWhoPaidIncorrect.value = true
//            return
//        }

        viewModelScope.launch {
            // TODO: DO it better without !!
            if (expenseType.value != null && amount.value != null && whoPaid != null) {
                insertExpense(expenseType.value!!, amount.value!!.toDouble(), whoPaid!!.id)
//                requireNotNull(activity).finish()
            }
        }
        _navigateToExpenses.value = true
    }

    fun onNavigatedToExpenses() {
        _navigateToExpenses.value = false
    }

    private suspend fun insertExpense(expenseType: String, amount: Double, selectedMemberId: Int) {
        withContext(Dispatchers.IO) {
            database.expenseDao.insertExpense(ExpenseEntry(expenseType, amount, selectedMemberId))
        }
    }
}