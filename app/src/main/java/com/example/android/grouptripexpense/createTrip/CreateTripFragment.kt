package com.example.android.grouptripexpense.createTrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.grouptripexpense.MainActivity
import com.example.android.grouptripexpense.R
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.databinding.FragmentCreateTripBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class CreateTripFragment: Fragment() {

    private lateinit var binding: FragmentCreateTripBinding
    private val viewModel by viewModels<CreateTripViewModel> {
        CreateTripViewModelFactory(AppDatabase.getInstance(requireNotNull(activity).application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Create Trip Fragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateTripBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireActivity()) { }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToAddMembers.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(R.id.action_createTripFragment_to_addMembersFragment)
                viewModel.onNavigatedToAddMembers()
            }
        })
//
//        viewModel.navigateToExpenses.observe(viewLifecycleOwner, {
//            if (it) {
//                this.findNavController().navigate(R.id.action_createTripFragment_to_expensesFragment)
//                viewModel.onNavigateToExpenses()
//            }
//        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        (requireActivity() as MainActivity).supportActionBar?.title = "Create a trip"

        println("Create Trip Fragment onCreateView")

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        println("Create Trip Fragment onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Create Trip Fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Create Trip Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Create Trip Fragment onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Create Trip Fragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Create Trip Fragment onDestroy")
    }
}