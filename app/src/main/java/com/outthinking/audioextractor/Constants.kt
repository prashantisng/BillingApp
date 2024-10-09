package com.outthinking.audioextractor

object Constants {
    // Use the fake local server data or real remote server.
    @Volatile
    var USE_FAKE_SERVER = true

    //Product IDs
    const val BASIC_PRODUCT = "basic_subscription"
    const val PREMIUM_PRODUCT = "premium_subscription"
    const val ONE_TIME_PRODUCT = "com.outthinking.audioextractor.lifetime"

    //Tags
    const val BASIC_MONTHLY_PLAN = "basicmonthly_1"
    const val BASIC_YEARLY_PLAN = "basicyearly_1"
    const val PREMIUM_MONTHLY_PLAN = "premiummonthly"
    const val PREMIUM_YEARLY_PLAN = "premiumyearly"
    const val BASIC_PREPAID_PLAN_TAG = "prepaidbasic"
    const val PREMIUM_PREPAID_PLAN_TAG = "prepaidpremium"


    const val PLAY_STORE_SUBSCRIPTION_URL
            = "https://play.google.com/store/account/subscriptions"
    const val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL
            = "https://play.google.com/store/account/subscriptions?product=%s&package=%s"


}