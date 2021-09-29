package com.example.android.grouptripexpense.summaryTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.grouptripexpense.MemberDividedAmount
import com.example.android.grouptripexpense.MemberDividedAmountAdapter
import com.example.android.grouptripexpense.R
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.database.ExpenseEntry
import com.example.android.grouptripexpense.database.MemberEntry
import com.example.android.grouptripexpense.databinding.FragmentSummaryTabBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class SummaryTabFragment : Fragment() {
    private var mMemberDividedAmountAdapter = MemberDividedAmountAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSummaryTabBinding.inflate(inflater, container, false)
        val factory = SummaryTabViewModelFactory(AppDatabase.getInstance(requireContext().applicationContext))
        val viewModel = ViewModelProvider(this, factory).get(SummaryTabViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.membersExpensesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                val members = it.first
                val expenseEntries = it.second
                var expensesTotal = 0.0
                val memberDividedAmounts: MutableList<MemberDividedAmount> = ArrayList()
                if (expenseEntries != null && members != null) {
                    if (expenseEntries.isNotEmpty()) {
                        expensesTotal = expenseEntries.stream().map { expense: ExpenseEntry -> expense.amount }.reduce { a: Double, b: Double -> a + b }.get()
                    }
                    binding.expensesTotal.text = expensesTotal.toString()
                    val membersCount = members.size
                    val dividedAmount: Double = if (membersCount != 0) expensesTotal / membersCount else 0.0
                    val dividedAmountBd = BigDecimal.valueOf(dividedAmount).setScale(2, RoundingMode.HALF_UP)
                    binding.dividedAmount.text = dividedAmountBd.toString()
                    for (expense in expenseEntries) {
                        val member = members.stream().filter { memberEntry: MemberEntry -> memberEntry.id == expense.memberId }.findFirst().get()
                        val foundMemberDividedAmountOptional = memberDividedAmounts.stream().filter { memberDividedAmount: MemberDividedAmount -> memberDividedAmount.memberName == member.name }.findFirst()
                        if (foundMemberDividedAmountOptional.isPresent) {
                            val foundMemberDividedAmount = foundMemberDividedAmountOptional.get()
                            foundMemberDividedAmount.paid = foundMemberDividedAmount.paid + expense.amount
                            val toPay = dividedAmountBd.toDouble() - foundMemberDividedAmount.paid
                            foundMemberDividedAmount.toPay = if (toPay < 0) 0.0 else BigDecimal.valueOf(toPay).setScale(2, RoundingMode.HALF_UP).toDouble()
                            val toReceive = foundMemberDividedAmount.paid - dividedAmountBd.toDouble()
                            foundMemberDividedAmount.toReceive = if (toReceive < 0) 0.0 else BigDecimal.valueOf(toReceive).setScale(2, RoundingMode.HALF_UP).toDouble()
                        } else {
                            val memberDividedAmount = MemberDividedAmount()
                            memberDividedAmount.memberName = member.name
                            memberDividedAmount.paid = expense.amount
                            val toPay = dividedAmountBd.toDouble() - memberDividedAmount.paid
                            memberDividedAmount.toPay = if (toPay < 0) 0.0 else BigDecimal.valueOf(toPay).setScale(2, RoundingMode.HALF_UP).toDouble()
                            val toReceive = memberDividedAmount.paid - dividedAmountBd.toDouble()
                            memberDividedAmount.toReceive = if (toReceive < 0) 0.0 else BigDecimal.valueOf(toReceive).setScale(2, RoundingMode.HALF_UP).toDouble()
                            memberDividedAmounts.add(memberDividedAmount)
                        }
                    }
                    for (memberEntry in members) {
                        val memberName = memberEntry.name
                        val memberDividedAmountOptional = memberDividedAmounts.stream().filter { memberDividedAmount: MemberDividedAmount -> memberDividedAmount.memberName == memberName }.findFirst()
                        if (!memberDividedAmountOptional.isPresent) {
                            val memberDividedAmount = MemberDividedAmount(memberName, 0.0, dividedAmountBd.toDouble(), 0.0)
                            memberDividedAmounts.add(memberDividedAmount)
                        }
                    }
                }
                val linearLayoutManager = LinearLayoutManager(context)
                binding.memberDividedAmount.layoutManager = linearLayoutManager
                binding.memberDividedAmount.setHasFixedSize(true)
                binding.memberDividedAmount.adapter = mMemberDividedAmountAdapter
                mMemberDividedAmountAdapter.setMemberDivideAmounts(memberDividedAmounts)
            }
        })

        viewModel.navigateToCreateTrip.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_expensesFragment_to_createTripFragment)
                viewModel.onNavigatedToCreateTrip()
            }
        })

        return binding.root
    }
}