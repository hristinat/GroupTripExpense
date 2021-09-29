package com.example.android.grouptripexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.android.grouptripexpense.databinding.FragmentExpensesBinding
import com.example.android.grouptripexpense.expenseTab.ExpenseTabFragment
import com.example.android.grouptripexpense.summaryTab.SummaryTabFragment
import com.google.android.material.transition.Hold

class ExpensesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        println("Expenses Fragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentExpensesBinding.inflate(inflater, container, false)
        setupViewPager(binding.container)
        binding.tabs.setupWithViewPager(binding.container)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        println("Expenses Fragment onCreateView")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Expenses Fragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Expenses Fragment onDestroy")
    }

    override fun onStart() {
        super.onStart()
        println("Expenses Fragment onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Expenses Fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Expenses Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Expenses Fragment onStop")
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPageAdapter(childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.addFragment(ExpenseTabFragment(), "Expense")
        adapter.addFragment(SummaryTabFragment(), "Summary")
        viewPager.adapter = adapter
    }
}