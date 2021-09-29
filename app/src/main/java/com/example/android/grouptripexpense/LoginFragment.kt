package com.example.android.grouptripexpense

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.grouptripexpense.LoginViewModel.*
import com.example.android.grouptripexpense.createTrip.CreateTripViewModel
import com.example.android.grouptripexpense.createTrip.CreateTripViewModelFactory
import com.example.android.grouptripexpense.database.AppDatabase
import com.example.android.grouptripexpense.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    private val createTripViewModel by viewModels<CreateTripViewModel> {
        CreateTripViewModelFactory(AppDatabase.getInstance(requireActivity().application))
    }

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthenticationState.AUTHENTICATED -> {
                    navController.navigate(R.id.createTripFragment)
                }
                AuthenticationState.UNAUTHENTICATED -> {
                    launchSignInFlow()
                }
                else -> Log.e(TAG, "Authentication state that doesn't require any UI change $it")
            }
        })

        createTripViewModel.navigateToAddMembers.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(R.id.addMembersFragment)
            }
        })

        createTripViewModel.navigateToExpenses.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(R.id.expensesFragment)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                Log.i(
                        TAG,
                        "Successfully signed in user " +
                                "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                // the back button. Otherwise check response.getError().getErrorCode() and handle
                // the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

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
                ).build(), SIGN_IN_RESULT_CODE
        )
    }
}

//class SignInActivity : AppCompatActivity(), View.OnClickListener {
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.sign_in_button -> signIn()
//        }
//    }
//
//    override fun onStart() {
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
//        super.onStart()
//    }
//
//    private fun signIn() {
//        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            updateUI(account)
//        } catch (e: ApiException) {
//            Log.w("MainActivity", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
//        }
//    }
//
//    private fun updateUI(account: GoogleSignInAccount?) {
////        if (account != null) {
//        val mainActivityIntent = Intent(this, MainActivity::class.java)
//        startActivity(mainActivityIntent)
//        //        }
//    }
//
//    companion object {
//        private const val RC_SIGN_IN = 1
//    }
//}