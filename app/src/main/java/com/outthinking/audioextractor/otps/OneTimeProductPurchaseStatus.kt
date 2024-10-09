package com.outthinking.audioextractor.otps

import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "oneTimeProductPurchases")
data class OneTimeProductPurchaseStatus(
    // Local fields.
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0,
    var isLocalPurchase: Boolean = false,
    var isAlreadyOwned: Boolean = false,

    // Remote fields.
    var product: String? = null,
    var purchaseToken: String? = null,
    var isEntitlementActive: Boolean = false,
    var isAcknowledged: Boolean = false,
    var isConsumed: Boolean = false,
    var quantity: Int = 0

)
