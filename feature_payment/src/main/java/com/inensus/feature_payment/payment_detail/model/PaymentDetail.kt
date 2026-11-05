package com.inensus.feature_payment.payment_detail.model

import com.google.gson.annotations.SerializedName
import com.inensus.core_network.model.PaymentDevice
import com.inensus.core_network.model.Token
import java.math.BigDecimal
import java.util.*

data class PaymentDetail(
    @SerializedName("original_transaction_id") val transactionId: Long,
    @SerializedName("original_transaction_type") val provider: String,
    @SerializedName("type") val type: String,
    @SerializedName("sender") val sender: String,
    @SerializedName("message") val meter: String,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("original_transaction") val originalTransaction: OriginalTransaction?,
    @SerializedName("sms") val sms: Sms?,
    @SerializedName("device") val paymentDevice: PaymentDevice?,
    @SerializedName("token") val token: Token?
)

data class OriginalTransaction(@SerializedName("status") val status: Int)

data class Sms(@SerializedName("receiver") val receiver: String, @SerializedName("body") val body: String)