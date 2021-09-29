package com.example.android.grouptripexpense.addMembers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.grouptripexpense.MainActivity
import com.example.android.grouptripexpense.R
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.databinding.FragmentAddMembersBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class AddMembersFragment : Fragment() {
    private lateinit var binding: FragmentAddMembersBinding
    private val viewModel by viewModels<AddMembersViewModel>() {
        AddMembersViewModelFactory(AppDatabase.getInstance(requireActivity()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Add members Fragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddMembersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (viewModel.membersNames.isNotEmpty()) {
            for (i in 0 until viewModel.membersNames.size) {
                addEditText()
            }
        }

        viewModel.trips.observe(viewLifecycleOwner, { tripEntries ->
            binding.tripName.text = tripEntries[0].name
            val emptyLines = tripEntries[0].membersCount - viewModel.membersNames.size
            for (i in 1 until emptyLines) {
                addEditText()
            }
        })

        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        (requireActivity() as MainActivity).supportActionBar?.title = "Add members"

        println("Add members Fragment onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.saveButtonClicked.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.membersNames = getMembersNames()
                viewModel.saveMembers()
                viewModel.saveButtonClicked()
            }
        })

        viewModel.isMembersNameEmpty.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(requireActivity(), "Please enter all member names.", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.sameMembersNames.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(requireActivity(), "All names should unique.", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.navigateToExpenses.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(R.id.action_addMembersFragment_to_expensesFragment)
                viewModel.navigatedToExpenses()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.membersNames = getMembersNames()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.membersNames = getMembersNames()
        println("Add Members Fragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.numberOfLines = 1
        println("Add Members Fragment onDestroy")
    }

    override fun onStart() {
        super.onStart()
        println("Add Members Fragment onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Add Members Fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Add Members Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Add Members Fragment onStop")
    }

    private fun addEditText() {
        viewModel.lastAddedEditText = TextInputLayout(requireActivity()).apply {
            startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_person_24)
            endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
        val lastAddedEditText = TextInputEditText(viewModel.lastAddedEditText!!.context)
        lastAddedEditText.hint = "Member name"
        val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (viewModel.numberOfLines == 1) {
            layoutParams.addRule(RelativeLayout.BELOW, R.id.member_input)
        } else {
            layoutParams.addRule(RelativeLayout.BELOW, viewModel.numberOfLines)
        }
        layoutParams.marginStart = 38
        layoutParams.marginEnd = 38
        viewModel.lastAddedEditText!!.layoutParams = layoutParams
        viewModel.lastAddedEditText!!.id = viewModel.numberOfLines + 1
        binding.membersLayout.addView(viewModel.lastAddedEditText, binding.membersLayout.childCount - 1)
        viewModel.lastAddedEditText!!.addView(lastAddedEditText)
        val buttonLayoutParams = binding.saveButton.layoutParams as RelativeLayout.LayoutParams
        buttonLayoutParams.addRule(RelativeLayout.BELOW, viewModel.numberOfLines + 1)
        binding.saveButton.layoutParams = buttonLayoutParams
        viewModel.numberOfLines++
    }

    private fun getMembersNames(): MutableList<String> {
        val membersNames = mutableListOf<String>()
        binding.membersLayout.forEachIndexed { _, view ->
            if (view is TextInputLayout) {
                val text = view.editText?.text.toString().trim()
                if (text.isNotEmpty()) {
                    membersNames.add(text)
                }
            }
        }
        return membersNames
    }
}