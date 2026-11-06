package com.inensus.shared_agent.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AgentRepository(private val service: AgentService, private val preferences: SharedPreferenceWrapper) {
    fun getMe() = service.getMe(preferences.baseUrl + GET_ME_ENDPOINT)

    /**
     * Caches the company's currency so amounts can be labelled with it.
     *
     * Every screen that prints an amount reads the cached value synchronously, so this is
     * fetched once per launch rather than per screen. A failure is deliberately swallowed:
     * the currency is a label, and losing it must not break a screen that is otherwise able
     * to show the amount itself.
     */
    fun refreshSettings() {
        service.getSettings(preferences.baseUrl + GET_SETTINGS_ENDPOINT)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response -> response.data?.currency?.let { preferences.currency = it } },
                { error -> Timber.e(error, "Company settings could not be fetched") }
            )
    }

    companion object {
        private const val GET_ME_ENDPOINT = "app/me"
        private const val GET_SETTINGS_ENDPOINT = "app/agents/settings"
    }
}
