package com.outthinking.audioextractor.ui.composable.home.otps

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outthinking.audioextractor.ui.composable.home.otps.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch



@Composable
fun OneTimeProductScreens(
    billingViewModel: BillingViewModel,
    oneTimeProductPurchaseStatusViewModel: OneTimeProductPurchaseStatusViewModel,
    modifier: Modifier = Modifier
) {
    val currentOneTimeProduct by
    oneTimeProductPurchaseStatusViewModel.currentOneTimeProductPurchase.collectAsState(
        initial = OneTimeProductPurchaseStatusViewModel.CurrentOneTimeProductPurchase.NONE
    )

    val otpContent by oneTimeProductPurchaseStatusViewModel.content.collectAsState(
        initial =
        ContentResource("google.com")
    )

    when (currentOneTimeProduct) {

        OneTimeProductPurchaseStatusViewModel.CurrentOneTimeProductPurchase.NONE -> {
            Surface(
                modifier = modifier,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    OneTimeProductPurchaseScreen(
                        billingViewModel = billingViewModel,
                    )
                }
            }
        }

        OneTimeProductPurchaseStatusViewModel.CurrentOneTimeProductPurchase
            .OTP -> {
            if (otpContent?.url != null) {
                OneTimeProductConsumptionScreen(
                    billingViewModel = billingViewModel,
                    otpContent = otpContent!!,
                    onRefresh = {
                        oneTimeProductPurchaseStatusViewModel.manualRefresh()
                    }
                )
            } else {
                LoadingScreen()
            }
        }
    }
}

@Composable
fun OneTimeProductPurchaseScreen(
    billingViewModel: BillingViewModel,
    modifier: Modifier = Modifier
) {
    val purchaseButtonClicked = remember { mutableStateOf(false) }

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ClassyTaxiScreenHeader(
                content = {
                    OneTimeProductsPurchaseButtons(
                        purchaseButtonClicked = purchaseButtonClicked,
                    )
                },
                textResource = R.string.otp_purchase_screen_text
            )
        }
    }

    if (purchaseButtonClicked.value) {
        billingViewModel.buyOneTimeProduct()
        purchaseButtonClicked.value = false
    }
}

@Composable
private fun OneTimeProductsPurchaseButtons(
    purchaseButtonClicked: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier)
    {
        Button(
            onClick = { purchaseButtonClicked.value = true },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding))
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.otp_purchase_button_text))
        }
    }
}


@Composable
fun OneTimeProductConsumptionScreen(
    billingViewModel: BillingViewModel,
    otpContent: ContentResource,
    onRefresh: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentOneTimeProducts = billingViewModel.oneTimeProductPurchases.collectAsState()
    val consumeCount = remember { mutableStateOf(0) }
    val maxConsumeCount = 1

    Card(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ClassyTaxiImage(
                contentDescription =
                stringResource(id = R.string.one_time_product_purchase_image_content_description),
                contentResource = otpContent,
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        consumeCount.value += 1
                        kotlin.runCatching {
                            currentOneTimeProducts.value.forEach { otp ->
                                otp.purchaseToken?.let { billingViewModel.consumePurchase(it) }
                            }
                        }.onSuccess {
                            onRefresh()
                        }.onFailure {
                            Log.e(
                                "OneTimeProductConsumptionScreen",
                                "Failed to consume purchase: ${it.message}"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = consumeCount.value < maxConsumeCount
            ) {
                Text(text = stringResource(id = R.string.consume_one_time_product_button_text))
            }
        }
    }
}

