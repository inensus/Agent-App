package com.inensus.feature_appliance.appliance_form.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class ConfirmApplianceRequest(
    @SerializedName("person_id") val customerId: Long?,
    @SerializedName("asset_id") val assetId: Long?,
    @SerializedName("cost") val cost: BigDecimal?,
    @SerializedName("down_payment") val downPayment: BigDecimal?,
    @SerializedName("rate") val rate: Int?,
    @SerializedName("rate_type") val rateType: String?,
    @SerializedName("first_payment_date") val firstPaymentDate: Date?,
    @SerializedName("device_serial") val deviceSerial: String?
)
