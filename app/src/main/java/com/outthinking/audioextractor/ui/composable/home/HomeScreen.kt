package com.outthinking.audioextractor.ui.composable.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.firebase.ui.auth.AuthUI
import com.outthinking.audioextractor.ui.composable.home.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassyTaxiApp(
    billingViewModel: BillingViewModel,
    subscriptionViewModel: SubscriptionStatusViewModel,
    oneTimeProductViewModel: OneTimeProductPurchaseStatusViewModel,
    authenticationViewModel: FirebaseUserViewModel,
    modifier: Modifier = Modifier,
) {
    ClassyTaxiAppKotlinTheme {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = Color.White,
                    content = {
                        MenuDrawer(items = {
                            DrawerMenuContent(
                                onRefresh = {
                                    oneTimeProductViewModel.manualRefresh()
                                    subscriptionViewModel.manualRefresh()
                                },
                                onSignOut = {
                                    subscriptionViewModel.unregisterInstanceId()
                                    AuthUI.getInstance().signOut(context).addOnCompleteListener {
                                        authenticationViewModel.updateFirebaseUser()
                                    }
                                },
                            )
                        })
                    })
            },
            content = {
                Scaffold(
                    topBar = {
                        ClassyTaxiTopBar {
                            coroutineScope.launch { drawerState.open() }
                        }
                    },
                ) { contentPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        MainNavigation(
                            billingViewModel = billingViewModel,
                            subscriptionStatusViewModel = subscriptionViewModel,
                            oneTimeProductPurchaseStatusViewModel = oneTimeProductViewModel,
                        )
                    }
                }
            },
        )
    }
}