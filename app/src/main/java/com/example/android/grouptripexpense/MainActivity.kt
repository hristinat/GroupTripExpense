package com.example.android.grouptripexpense

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.android.grouptripexpense.createTrip.CreateTripViewModel
import com.example.android.grouptripexpense.createTrip.CreateTripViewModelFactory
import com.example.android.grouptripexpense.database.AppDatabase
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CreateTripViewModel> {
        CreateTripViewModelFactory(AppDatabase.getInstance(application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("Activity onCreate")
//        viewModel.navigateToAddMembers.observe(this, {
//            if (it) {
//                this.findNavController(R.id.nav_host_fragment).navigate(R.id.addMembersFragment)
//            }
//        })
//
//        viewModel.navigateToExpenses.observe(this, {
//            if (it) {
//                this.findNavController(R.id.nav_host_fragment).navigate(R.id.expensesFragment)
//            }
//        })
    }

    override fun onStart() {
        super.onStart()
        println("Activity onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Activity onResume")
    }

    override fun onPause() {
        super.onPause()
        println("Activity onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Activity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Activity onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        println("Activity onRestart")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                AuthUI.getInstance().signOut(this)
                launchSignInFlow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //TODO: fix duplicate code and check if this could be only here
    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email or Google account. If users
        // choose to register with their email, they will need to create a password as well.
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent. We listen to the response of this activity with the
        // SIGN_IN_RESULT_CODE code.
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        providers
                ).build(), LoginFragment.SIGN_IN_RESULT_CODE
        )
    }
}