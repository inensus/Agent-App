package com.inensus.shared_agent.model

import com.google.gson.annotations.SerializedName

/**
 * The company's main settings, as served by `GET app/agents/settings`.
 *
 * Only the fields the app actually renders are mapped; the endpoint serves the same payload
 * the web admin reads, so it carries more than this.
 */
data class MainSettingsResponse(
    @SerializedName("data") val data: MainSettings?
)

data class MainSettings(
    @SerializedName("currency") val currency: String?,
    @SerializedName("company_name") val companyName: String?,
    @SerializedName("country") val country: String?
)
