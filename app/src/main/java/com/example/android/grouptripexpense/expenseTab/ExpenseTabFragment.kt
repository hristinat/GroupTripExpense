package com.example.android.grouptripexpense.expenseTab

import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.grouptripexpense.Expense
import com.example.android.grouptripexpense.ExpenseAdapter
import com.example.android.grouptripexpense.MainViewModel
import com.example.android.grouptripexpense.R
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.MemberEntry
import com.example.android.grouptripexpense.databinding.FragmentExpensesTabBinding
import com.google.android.material.transition.Hold
import java.util.*

class ExpenseTabFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentExpensesTabBinding.inflate(inflater, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        binding.expensesList.layoutManager = linearLayoutManager
        val mExpenseAdapter = ExpenseAdapter()
        binding.expensesList.setHasFixedSize(true)
        binding.expensesList.adapter = mExpenseAdapter
        val database = AppDatabase.getInstance(requireActivity().application)
        val factory = ExpenseTabViewModelFactory(database)
        val viewModel = ViewModelProvider(this, factory).get(ExpenseTabViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToAddExpense.observe(viewLifecycleOwner, {
            if (it) {
                val extras = FragmentNavigatorExtras(binding.addFab to "shared_element_container")
                this.findNavController().navigate(R.id.action_expensesFragment_to_addExpenseFragment, null, null, extras)
                binding.addFab.setBackgroundColor(WHITE)
                viewModel.onNavigatedToAddExpense()
            }
        })

        viewModel.membersExpensesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                val members = it.first
                val expenseEntries = it.second
                if (members != null && expenseEntries != null) {
                    binding.noExpensesYet.visibility = if (expenseEntries.isEmpty()) View.VISIBLE else View.INVISIBLE

                    val expenses: MutableList<Expense> = ArrayList()
                    for (expenseEntry in expenseEntries) {
                        var memberEntry: MemberEntry
                        memberEntry = if (members.isEmpty()) {
                            return@observe
                        } else {
                            members.stream().filter { mE: MemberEntry -> mE.id == expenseEntry.memberId }.findFirst().get()
                        }
                        expenses.add(Expense(expenseEntry.type, expenseEntry.amount, memberEntry.name))
                    }
                    mExpenseAdapter.setExpenses(expenses)
                }
            }
        })

//        viewModel.members.observe(viewLifecycleOwner, { memberEntries -> members = memberEntries })
//        viewModel.expenses.observe(viewLifecycleOwner, Observer { expenseEntries ->
//            binding.noExpensesYet.visibility = if (expenseEntries.isEmpty()) View.VISIBLE else View.INVISIBLE
//            val expenses: MutableList<Expense> = ArrayList()
//            for (expenseEntry in expenseEntries) {
//                var memberEntry: MemberEntry
//                memberEntry = if (members.isEmpty()) {
//                    return@Observer
//                } else {
//                    members.stream().filter { mE: MemberEntry -> mE.id == expenseEntry.memberId }.findFirst().get()
//                }
//                expenses.add(Expense(expenseEntry.type, expenseEntry.amount, memberEntry.name))
//            }
//            mExpenseAdapter.setExpenses(expenses)
//        })

        return binding.root
    }


}