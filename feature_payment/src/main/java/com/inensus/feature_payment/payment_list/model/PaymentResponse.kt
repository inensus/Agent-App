package com.inensus.feature_payment.payment_list.model

import com.google.gson.annotations.SerializedName
import com.inensus.core_network.model.Payment

data class PaymentResponse(
    @SerializedName("next_page_url") val nextPageUrl: String?,
    @SerializedName("data") val data: List<Payment>
)