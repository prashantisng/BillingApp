package com.outthinking.audioextractor.ui.composable.home.subscriptions

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource

@Composable
fun BasicTabScreens(
    currentSubscription: SubscriptionStatusViewModel.CurrentSubscription,
    contentResource: ContentResource?,
    billingViewModel: BillingViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedBasicConversionButton =
        remember { mutableStateOf(SelectedSubscriptionBasePlan.NONE.index) }

    val selectedEntitlementButtonMonthly =
        remember { mutableStateOf(SelectedSubscriptionBasePlan.NONE.index) }

    val selectedEntitlementButtonPrepaid =
        remember { mutableStateOf(SelectedSubscriptionBasePlan.NONE.index) }

    when (currentSubscription) {
        SubscriptionStatusViewModel.CurrentSubscription.BASIC_PREPAID -> {
            if (contentResource != null) {
                BasicEntitlementScreen(
                    contentResource = contentResource,
                    buttons = {
                        BasicPrepaidEntitlementButtons(
                            selectedEntitlementButtonPrepaid = selectedEntitlementButtonPrepaid
                        )
                    },
                    message = R.string.current_prepaid_basic_subscription_message,
                    currentBasicPlan = Constants.BASIC_PREPAID_PLAN_TAG,
                    selectedButton = selectedEntitlementButtonPrepaid,
                    billingViewModel = billingViewModel
                )
            } else {
                LoadingScreen()
            }
        }

        SubscriptionStatusViewModel.CurrentSubscription.BASIC_RENEWABLE -> {
            if (contentResource != null) {
                BasicEntitlementScreen(
                    contentResource = contentResource,
                    buttons = {
                        BasicMonthlyEntitlementButtons(
                            selectedEntitlementButtonMonthly = selectedEntitlementButtonMonthly,
                        )
                    },
                    message = R.string.current_recurring_basic_subscription_message,
                    currentBasicPlan = Constants.BASIC_MONTHLY_PLAN,
                    selectedButton = selectedEntitlementButtonMonthly,
                    billingViewModel = billingViewModel
                )
            } else {
                LoadingScreen()
            }
        }

        SubscriptionStatusViewModel.CurrentSubscription.PREMIUM_RENEWABLE -> {
            BasicConversionScreen(
                billingViewModel = billingViewModel,
                buttons = {
                    BasicDowngradeButtons(
                        selectedBasicConversionButton = selectedBasicConversionButton
                    )
                },
                selectedButton = selectedBasicConversionButton,
                message = R.string.current_recurring_premium_subscription_message,
            )
        }

        SubscriptionStatusViewModel.CurrentSubscription.PREMIUM_PREPAID -> {
            BasicConversionScreen(
                billingViewModel = billingViewModel,
                buttons = {
                    BasicDowngradeButtons(
                        selectedBasicConversionButton = selectedBasicConversionButton
                    )
                },
                selectedButton = selectedBasicConversionButton,
                message = R.string.current_prepaid_premium_subscription_message,
            )
        }

        SubscriptionStatusViewModel.CurrentSubscription.NONE -> {
            BasicBasePlansScreen(billingViewModel = billingViewModel)
        }
    }
}

@Composable
fun BasicBasePlansScreen(
    billingViewModel: BillingViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedBasicSubscriptionButton =
        remember { mutableStateOf(SelectedSubscriptionBasePlan.NONE.index) }

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClassyTaxiScreenHeader(
                content = {
                    BasePlanScreenButtons(
                        selectedBasicSubscriptionButton = selectedBasicSubscriptionButton,
                    )
                },
                textResource = R.string.basic_paywall_message
            )
        }
    }

    LaunchedEffect(selectedBasicSubscriptionButton.value) {
        when (selectedBasicSubscriptionButton.value) {
            SelectedSubscriptionBasePlan.MONTHLY.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_MONTHLY_PLAN,
                    upDowngrade = false
                )
                resetSelectedButton(
                    selectedIntButton = selectedBasicSubscriptionButton,
                    selectedBooleanButton = null
                )
            }

            SelectedSubscriptionBasePlan.YEARLY.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_YEARLY_PLAN,
                    upDowngrade = false
                )
                resetSelectedButton(
                    selectedIntButton = selectedBasicSubscriptionButton,
                    selectedBooleanButton = null
                )
            }

            SelectedSubscriptionBasePlan.PREPAID.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_PREPAID_PLAN_TAG,
                    upDowngrade = false
                )
                resetSelectedButton(
                    selectedIntButton = selectedBasicSubscriptionButton,
                    selectedBooleanButton = null
                )
            }
        }
    }
}

@Composable
fun BasicEntitlementScreen(
    contentResource: ContentResource,
    @StringRes message: Int,
    currentBasicPlan: String,
    selectedButton: MutableState<Int>,
    billingViewModel: BillingViewModel,
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ClassyTaxiImage(
            contentDescription = "Basic Entitlement",
            contentResource = contentResource
        )

        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ClassyTaxiScreenHeader(content = buttons, textResource = message)
            }
        }
    }

    LaunchedEffect(key1 = currentBasicPlan, key2 = selectedButton.value) {
        when (currentBasicPlan) {
            in listOf(Constants.BASIC_MONTHLY_PLAN, Constants.BASIC_YEARLY_PLAN) -> {
                if (selectedButton.value == SelectedSubscriptionBasePlan.PREPAID.index) {
                    billingViewModel.buyBasePlans(
                        product = Constants.BASIC_PRODUCT,
                        tag = Constants.BASIC_PREPAID_PLAN_TAG,
                        upDowngrade = true
                    )
                    resetSelectedButton(
                        selectedIntButton = selectedButton,
                        selectedBooleanButton = null
                    )
                }
            }

            Constants.BASIC_PREPAID_PLAN_TAG -> {
                when (selectedButton.value) {
                    SelectedSubscriptionBasePlan.MONTHLY.index -> {
                        billingViewModel.buyBasePlans(
                            product = Constants.BASIC_PRODUCT,
                            tag = Constants.BASIC_MONTHLY_PLAN,
                            upDowngrade = true
                        )
                        resetSelectedButton(
                            selectedIntButton = selectedButton,
                            selectedBooleanButton = null
                        )
                    }

                    SelectedSubscriptionBasePlan.YEARLY.index -> {
                        billingViewModel.buyBasePlans(
                            product = Constants.BASIC_PRODUCT,
                            tag = Constants.BASIC_YEARLY_PLAN,
                            upDowngrade = false
                        )
                        resetSelectedButton(
                            selectedIntButton = selectedButton,
                            selectedBooleanButton = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BasicConversionScreen(
    billingViewModel: BillingViewModel,
    selectedButton: MutableState<Int>,
    @StringRes message: Int,
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClassyTaxiScreenHeader(content = buttons, textResource = message)
        }
    }

    LaunchedEffect(selectedButton.value) {
        when (selectedButton.value) {
            SelectedSubscriptionBasePlan.MONTHLY.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_MONTHLY_PLAN,
                    upDowngrade = true
                )
                resetSelectedButton(
                    selectedIntButton = selectedButton,
                    selectedBooleanButton = null
                )
            }

            SelectedSubscriptionBasePlan.YEARLY.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_YEARLY_PLAN,
                    upDowngrade = true
                )
                resetSelectedButton(
                    selectedIntButton = selectedButton,
                    selectedBooleanButton = null
                )
            }

            SelectedSubscriptionBasePlan.PREPAID.index -> {
                billingViewModel.buyBasePlans(
                    product = Constants.BASIC_PRODUCT,
                    tag = Constants.BASIC_PREPAID_PLAN_TAG,
                    upDowngrade = true
                )
                resetSelectedButton(
                    selectedIntButton = selectedButton,
                    selectedBooleanButton = null
                )
            }
        }
    }
}

@Composable
private fun BasePlanScreenButtons(
    selectedBasicSubscriptionButton: MutableState<Int>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                println("pay monthly ==> ${SelectedSubscriptionBasePlan.MONTHLY.index}")
                selectedBasicSubscriptionButton.value = SelectedSubscriptionBasePlan.MONTHLY.index
            }) {
            Text(text = stringResource(id = R.string.monthly_basic_plan_text))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedBasicSubscriptionButton.value = SelectedSubscriptionBasePlan.YEARLY.index
            }) {
            Text(text = stringResource(id = R.string.yearly_basic_plan_text))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedBasicSubscriptionButton.value = SelectedSubscriptionBasePlan.PREPAID.index
            }) {
            Text(text = stringResource(id = R.string.prepaid_basic_plan_text))
        }
    }
}

@Composable
private fun BasicDowngradeButtons(
    selectedBasicConversionButton: MutableState<Int>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedBasicConversionButton.value = SelectedSubscriptionBasePlan.MONTHLY.index
            }) {
            Text(text = stringResource(id = R.string.downgrade_to_basic_monthly))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedBasicConversionButton.value = SelectedSubscriptionBasePlan.YEARLY.index
            }) {
            Text(text = stringResource(id = R.string.downgrade_to_basic_yearly))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedBasicConversionButton.value = SelectedSubscriptionBasePlan.PREPAID.index
            }) {
            Text(text = stringResource(id = R.string.downgrade_to_basic_prepaid))
        }
    }
}

@Composable
private fun BasicMonthlyEntitlementButtons(
    selectedEntitlementButtonMonthly: MutableState<Int>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedEntitlementButtonMonthly.value = SelectedSubscriptionBasePlan.PREPAID.index
            }) {
            Text(text = stringResource(id = R.string.convert_to_basic_prepaid))
        }
    }
}

@Composable
private fun BasicPrepaidEntitlementButtons(
    selectedEntitlementButtonPrepaid: MutableState<Int>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedEntitlementButtonPrepaid.value = SelectedSubscriptionBasePlan.MONTHLY.index
            }) {
            Text(text = stringResource(id = R.string.convert_to_basic_monthly))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.menu_drawer_body_button_padding)),
            onClick = {
                selectedEntitlementButtonPrepaid.value = SelectedSubscriptionBasePlan.YEARLY.index
            }) {
            Text(text = stringResource(id = R.string.convert_to_basic_yearly))
        }
    }
}
