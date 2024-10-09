package com.outthinking.audioextractor

import com.google.firebase.messaging.FirebaseMessagingService

class FcmRegistrationTokenService: FirebaseMessagingService() {
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        val app = application as BillingApp
        app.repository.registerInstanceId(token)
    }

}