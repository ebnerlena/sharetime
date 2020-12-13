package com.lenaebner.sharetime

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.Scopes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.LoginFragmentBinding

class LoginFragment : Fragment(R.layout.login_fragment){

    private val providers =  listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().setScopes(listOf(Scopes.PROFILE)).build(),
        AuthUI.IdpConfig.GitHubBuilder().build()
    )

    private var userId: String = ""
    private val db = Firebase.firestore.collection("users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = LoginFragmentBinding.bind(view)

        if (Firebase.auth.currentUser == null){
            performSilentLogin()
        }
        else {
            userId = Firebase.auth.currentUser?.uid.orEmpty()
            findNavController().navigate(LoginFragmentDirections.loginToHome(userId))
        }
    }

    private fun performSilentLogin() {
        AuthUI.getInstance().silentSignIn(requireContext(), providers).addOnCompleteListener {
            if (it.isSuccessful) {
                userId = Firebase.auth.currentUser?.uid.orEmpty()
                findNavController().navigate(LoginFragmentDirections.loginToHome(userId))

            } else {
                //The user could not be silently logged in so we start the UI for authentication
                performLogin()
            }
        }
    }

    private fun performLogin() {
        val authIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
            .build()

        startActivityForResult(authIntent, REQUEST_CODE)
    }
    private fun showError(message: String) =
        Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { performLogin() }
            .show()


    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (response == null) {
                showError("something went wrong... try again please")
            }

            if(response?.error == null) {
                //no error
                if (response != null) {
                    userId = Firebase.auth.currentUser?.uid.orEmpty()

                    if(response.isNewUser){
                        val person = Person(response.user.name.toString(),"That's me: ", response.user.photoUri.toString())
                        db.document(userId).set(person)

                        findNavController().navigate(LoginFragmentDirections.loginToEditProfile(userId))
                    }
                    else {
                        findNavController().navigate(LoginFragmentDirections.loginToHome(userId))
                    }
                }
            }
            else {
                showError(response?.error?.message ?: "Something went wrong...")
            }
        }
    }

    private fun setupBottomNavigation() {
        //if user has profile image set it as icon in bottom navigatino
    }
}