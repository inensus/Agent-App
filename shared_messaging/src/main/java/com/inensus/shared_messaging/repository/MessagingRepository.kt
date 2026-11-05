package com.inensus.shared_messaging.repository

import com.google.firebase.iid.FirebaseInstanceId
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.shared_messaging.model.MessagingRequest
import timber.log.Timber

class MessagingRepository(private val service: MessagingService, private val preferences: SharedPreferenceWrapper) {

    /**
     * Reads the current FCM token from Firebase and registers it with the backend.
     *
     * Relying on MerchantFirebaseMessagingService.onNewToken alone is not enough: that
     * callback only fires when Firebase mints or rotates a token, which on an existing
     * install happens long before the agent ever signs in. If the single registration
     * attempt that follows it fails, or the agent signs in on a device whose token was
     * issued earlier, the backend keeps an empty `fire_base_token` and has no way to reach
     * the device, with no second chance. Asking Firebase for the token directly makes
     * registration independent of that one callback.
     */
    fun registerDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { result ->
                preferences.firebaseDeviceToken = result.token
                sendToken(result.token)
            }
            .addOnFailureListener { error ->
                Timber.e(error, "FCM token could not be read")
            }
    }

    fun resetFirebaseToken() = registerDeviceToken()

    private fun sendToken(token: String) {
        service.resetFirebaseToken(preferences.baseUrl + RESET_FIREBASE_TOKEN_ENDPOINT, MessagingRequest(token))
            .subscribe(
                { preferences.resetFirebase = false },
                { error -> Timber.e(error, "FCM token could not be registered with the backend") }
            )
    }

    companion object {
        private const val RESET_FIREBASE_TOKEN_ENDPOINT = "app/agents/firebase"
    }
}
