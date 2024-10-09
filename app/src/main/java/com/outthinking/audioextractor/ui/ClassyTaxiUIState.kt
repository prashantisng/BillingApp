package com.outthinking.audioextractor.ui

import com.android.billingclient.api.Purchase

data class ClassyTaxiUIState(
    val hasRenewableBasic: Boolean? = false,
    val hasPrepaidBasic: Boolean? = false,
    val hasRenewablePremium: Boolean? = false,
    val hasPrepaidPremium: Boolean? = false,
    val purchases: List<Purchase>? = null,
)
