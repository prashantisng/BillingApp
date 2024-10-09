package com.outthinking.audioextractor

import android.app.Application
import com.dev.audioextractor.data.BillingRepository
import com.outthinking.audioextractor.data.SubscriptionPurchasesDatabase
import com.outthinking.audioextractor.data.disk.OneTimePurchasesDatabase
import com.outthinking.audioextractor.gpbl.BillingClientLifecycle

class BillingApp :Application() {
    private val subscriptionsDatabase: SubscriptionPurchasesDatabase
        get() = SubscriptionPurchasesDatabase.getInstance(this)

    private val  oneTimeProductPurchasesDatabase: OneTimePurchasesDatabase
        get() = OneTimePurchasesDatabase.getInstance(this)


    private val billingLocalDataSource: BillingLocalDataSource
        get() = BillingLocalDataSource.getInstance(
            subscriptionsDatabase.subscriptionStatusDao(),
            oneTimeProductPurchasesDatabase.oneTimeProductStatusDao()
        )

/*    private val serverFunctions: ServerFunctions
        get() {
            return if (USE_FAKE_SERVER) {
                FakeServerFunctions.getInstance()
            } else {
                ServerFunctions.getInstance()
            }
        }*/



    val billingClientLifecycle: BillingClientLifecycle
        get() = BillingClientLifecycle.getInstance(this)

    val repository: BillingRepository
        get() = BillingRepository.getInstance(billingLocalDataSource, billingClientLifecycle)

}