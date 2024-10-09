package com.outthinking.audioextractor.utils

import android.content.res.Resources
import com.dev.audioextractor.data.subscriptions.SubscriptionStatus
import com.outthinking.audioextractor.R
import com.outthinking.audioextractor.gpbl.isAccountHold
import com.outthinking.audioextractor.gpbl.isBasicContent
import com.outthinking.audioextractor.gpbl.isGracePeriod
import com.outthinking.audioextractor.gpbl.isPaused
import com.outthinking.audioextractor.gpbl.isPremiumContent
import com.outthinking.audioextractor.gpbl.isSubscriptionRestore


/**
 * Return the resource string for the basic subscription button.
 *
 * Add an asterisk if the subscription is not local and might not be modifiable on this device.
 */
fun basicTextForSubscription(res: Resources, subscription: SubscriptionStatus): String {
    val text = when {
        isAccountHold(subscription) -> {
            res.getString(R.string.subscription_option_basic_message_account_hold)
        }
        isPaused(subscription) -> {
            res.getString(R.string.subscription_option_basic_message_account_paused)
        }
        isGracePeriod(subscription) -> {
            res.getString(R.string.subscription_option_basic_message_grace_period)
        }
        isSubscriptionRestore(subscription) -> {
            res.getString(R.string.subscription_option_basic_message_restore)
        }
        subscription.isBasicContent -> {
            res.getString(R.string.subscription_option_basic_message_current)
        }
        else -> {
            res.getString(R.string.subscription_option_basic_message)
        }
    }
    return if (subscription.isLocalPurchase) {
        text
    } else {
        // No local record, so the subscription cannot be managed on this device.
        "$text*"
    }
}

/**
 * Return the resource string for the premium subscription button.
 *
 * Add an asterisk if the subscription is not local and might not be modifiable on this device.
 */
fun premiumTextForSubscription(res: Resources, subscription: SubscriptionStatus): String {
    val text = when {
        isAccountHold(subscription) -> {
            res.getString(R.string.subscription_option_premium_message_account_hold)
        }

        isPaused(subscription) -> {
            res.getString(R.string.subscription_option_premium_message_account_paused)
        }

        isGracePeriod(subscription) -> {
            res.getString(R.string.subscription_option_premium_message_grace_period)
        }

        isSubscriptionRestore(subscription) -> {
            res.getString(R.string.subscription_option_premium_message_restore)
        }

        isPremiumContent(subscription) -> {
            res.getString(R.string.subscription_option_premium_message_current)
        }

        else -> {
            res.getString(R.string.subscription_option_premium_message)
        }
    }
    return if (subscription.isLocalPurchase) {
        text
    } else {
        // No local record, so the subscription cannot be managed on this device.
        "$text*"
    }
}