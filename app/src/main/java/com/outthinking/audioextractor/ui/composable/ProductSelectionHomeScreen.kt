package com.outthinking.audioextractor.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.outthinking.audioextractor.R


@Composable
fun ProductSelectionScreen(
    onNavigateToOneTimeProductScreen: () -> Unit,
    onNavigateToSubscriptionScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ClassyTaxiScreenHeader(
                content = { ProductSelectionButtons(
                    onNavigateToOneTimeProductScreen = onNavigateToOneTimeProductScreen,
                    onNavigateToSubscriptionScreen = onNavigateToSubscriptionScreen,
                ) },
                textResource = R.string.product_selection_screen_text
            )
        }
    }
}

@Composable
private fun ProductSelectionButtons(
    onNavigateToOneTimeProductScreen: () -> Unit,
    onNavigateToSubscriptionScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = { onNavigateToOneTimeProductScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding))
        ) {
            Text(text = stringResource(id = R.string.otp_selection_button_text))
        }
        Button(
            onClick = { onNavigateToSubscriptionScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding))
        ) {
            Text(text = stringResource(id = R.string.subscription_selection_button_text))
        }
    }
}