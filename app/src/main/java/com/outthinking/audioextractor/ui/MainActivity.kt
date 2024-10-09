package com.outthinking.audioextractor.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.Purchase
import com.firebase.ui.auth.AuthUI
import com.outthinking.audioextractor.BillingApp
import com.outthinking.audioextractor.Constants
import com.outthinking.audioextractor.gpbl.BillingClientLifecycle
import kotlinx.coroutines.launch

class MainActivity :AppCompatActivity() {
    private lateinit var billingClientLifecycle: BillingClientLifecycle

    private lateinit var authenticationViewModel: FirebaseUserViewModel
    private lateinit var billingViewModel: BillingViewModel
    private lateinit var subscriptionViewModel: SubscriptionStatusViewModel
    private lateinit var oneTimePurchaseViewModel: OneTimeProductPurchaseStatusViewModel

    private val registerResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            Log.d(TAG, "Sign-in SUCCESS!")
            authenticationViewModel.updateFirebaseUser()
        } else {
            Log.d(TAG, "Sign-in FAILED!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        authenticationViewModel = ViewModelProvider(this)[FirebaseUserViewModel::class.java]
        billingViewModel = ViewModelProvider(this)[BillingViewModel::class.java]
        subscriptionViewModel =
            ViewModelProvider(this)[SubscriptionStatusViewModel::class.java]
        oneTimePurchaseViewModel =
            ViewModelProvider(this)[OneTimeProductPurchaseStatusViewModel::class.java]

        // Billing APIs are all handled in the this lifecycle observer.
        billingClientLifecycle = (application as BillingApp).billingClientLifecycle
        lifecycle.addObserver(billingClientLifecycle)

        // Launch the billing flow when the user clicks a button to buy something.
        billingViewModel.buyEvent.observe(this) {
            if (it != null) {
                billingClientLifecycle.launchBillingFlow(this, it)
            }
        }

        // Open the Play Store when this event is triggered.
        billingViewModel.openPlayStoreSubscriptionsEvent.observe(this) { product ->
            Log.i(TAG, "Viewing subscriptions on the Google Play Store")
            val url = if (product == null) {
                // If the Product is not specified, just open the Google Play subscriptions URL.
                Constants.PLAY_STORE_SUBSCRIPTION_URL
            } else {
                // If the Product is specified, open the deeplink for this Product on Google Play.
                String.format(Constants.PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL, product, packageName)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        // Update authentication UI.
        authenticationViewModel.firebaseUser.observe(this) {
            invalidateOptionsMenu()
            if (it == null) {
                triggerSignIn()
            } else {
                Log.d(TAG, "CURRENT user: ${it.email} ${it.displayName}")
            }
        }

        // Update purchases information when user changes.
        authenticationViewModel.userChangeEvent.observe(this) {
            subscriptionViewModel.userChanged()
            lifecycleScope.launch {
                registerPurchases(billingClientLifecycle.subscriptionPurchases.value)
                registerPurchases(billingClientLifecycle.oneTimeProductPurchases.value)
            }
        }

        super.onCreate(savedInstanceState)
        setContent {
            ClassyTaxiApp(
                billingViewModel = billingViewModel,
                subscriptionViewModel = subscriptionViewModel,
                authenticationViewModel = authenticationViewModel,
                oneTimeProductViewModel = oneTimePurchaseViewModel,
            )
        }
    }


    /**
     * Register Product purchases with the server.
     */
    private suspend fun registerPurchases(purchaseList: List<Purchase>) {
        billingViewModel.registerPurchases(purchaseList)
    }

    /**
     * Sign in with FirebaseUI Auth.
     */
    private fun triggerSignIn() {
        Log.d(TAG, "Attempting SIGN-IN!")
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            // AuthUI.IdpConfig.GoogleBuilder().build()
        )
        registerResult.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}