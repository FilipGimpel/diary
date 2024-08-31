package com.gimpel.diary.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.gimpel.diary.presentation.navigation.DiaryApp
import com.gimpel.diary.ui.theme.DiaryTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result: FirebaseAuthUIAuthenticationResult? ->
            setupNavigation()
        }

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            setupNavigation()
        } else {
            val signInIntent: Intent = AuthUI.getInstance().createSignInIntentBuilder()
                // here you can add/remove login providers
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build())).build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun setupNavigation() {
        setContent {
            DiaryTheme {
                DiaryApp()
            }
        }
    }
}