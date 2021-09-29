package com.example.android.grouptripexpense.addExpense

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.grouptripexpense.MainActivity
import com.example.android.grouptripexpense.R
import com.example.android.grouptripexpense.TripWidgetService
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.databinding.FragmentAddExpenseBinding
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialContainerTransform.*

class AddExpenseFragment : Fragment() {
    private val viewModel by viewModels<AddExpenseViewModel> {
        AddExpenseViewModelFactory(AppDatabase.getInstance(requireActivity()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            startContainerColor = Color.WHITE
            endContainerColor = Color.WHITE
            containerColor = Color.WHITE
            fadeMode = FADE_MODE_OUT
        }

        println("Add Expenses Fragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.membersNames.observe(viewLifecycleOwner, {
            if (it?.isNotEmpty() == true) {
                val adapter = ArrayAdapter(requireActivity(), R.layout.menu_item, it)
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                (binding.whoPaid.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                if (it.size == 1) {
                    binding.whoPaidMenu.setText(it[0], true)
                    viewModel.whoPaidItemIdPosition.value = 0
                }
                binding.whoPaidMenu.inputType = InputType.TYPE_NULL

                //TODO: check if there is a better approach and check if it's okay to use livedata without observe?
                if (viewModel.whoPaidItemIdPosition.value != null) {
//                    binding.whoPaidSpinner.setSelection(viewModel.whoPaidItemIdPosition.value!!)
                }
            }
        })

        viewModel.isWhoPaidIncorrect.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(requireActivity(), "Please enter valid member name.", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.navigateToExpenses.observe(viewLifecycleOwner, {
            if (it) {
                TripWidgetService.startActionUpdateTripWidget(requireActivity())
                findNavController().navigate(R.id.action_addExpenseFragment_to_expensesFragment)
                viewModel.onNavigatedToExpenses()
            }
        })

        binding.whoPaidMenu.setOnItemClickListener { parent, view, position, id ->
            viewModel.whoPaidItemIdPosition.value = id
        }

        (requireActivity() as MainActivity).supportActionBar?.title = "Add expense"

        println("Add Expenses Fragment onCreateView")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Add Expenses Fragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Add Expenses Fragment onDestroy")
    }

    override fun onStart() {
        super.onStart()
        println("Add Expenses Fragment onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Add Expenses Fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Add Expenses Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Add Expenses Fragment onStop")
    }

}