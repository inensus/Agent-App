package com.inensus.core.sharedpreferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.inensus.core.BuildConfig

class SharedPreferenceWrapper(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var accessToken: String
        get() = preferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
        set(value) {
            preferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()
        }

    var baseUrl: String?
        get() = preferences.getString(KEY_BASE_URL, DEFAULT_BASE_URL)
        set(url) {
            preferences.edit().putString(KEY_BASE_URL, url + BASE_URL_SUFFIX).apply()
        }

    var deviceId: String
        get() = preferences.getString(KEY_DEVICE_ID, "") ?: ""
        set(value) {
            preferences.edit().putString(KEY_DEVICE_ID, value).apply()
        }

    var firebaseDeviceToken: String
        get() = preferences.getString(KEY_FIREBASE_DEVICE_TOKEN, "") ?: ""
        set(value) {
            preferences.edit().putString(KEY_FIREBASE_DEVICE_TOKEN, value).apply()
        }

    var resetFirebase: Boolean
        get() = preferences.getBoolean(KEY_RESET_FIREBASE, true)
        set(value) {
            preferences.edit().putBoolean(KEY_RESET_FIREBASE, value).apply()
        }

    companion object {
        // Set per build type in core/build.gradle.kts:
        // release -> https://cloud.micropowermanager.com/api/
        // debug   -> http://api.mpmanager.local/api/
        private val DEFAULT_BASE_URL = BuildConfig.DEFAULT_BASE_URL
        const val BASE_URL_SUFFIX = "/api/"

        private const val KEY_BASE_URL = "baseUrl"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_FIREBASE_DEVICE_TOKEN = "firebase_device_token"
        private const val KEY_RESET_FIREBASE = "reset_firebase"
    }
}
