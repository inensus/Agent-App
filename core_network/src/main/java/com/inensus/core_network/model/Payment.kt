package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class Payment(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String?,
    @SerializedName("sender") val sender: String,
    @SerializedName("message") val message: String?,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("transaction") val transaction: Transaction?,
    @SerializedName("original_agent") val originalAgent: OriginalAgent?,
    @SerializedName("device") val device: PaymentDevice?,
    @SerializedName("token") val token: Token?
) : Parcelable

@Parcelize
data class Transaction(@SerializedName("type") val type: String, @SerializedName("message") val message: String) : Parcelable

@Parcelize
data class OriginalAgent(@SerializedName("status") val status: Int) : Parcelable

/**
 * The device a transaction belongs to. The backend used to send this as `meter`, with the
 * owner nested under `meter_parameter.owner`; it is now `device`, with the owner under `person`.
 */
@Parcelize
data class PaymentDevice(
    @SerializedName("device_serial") val deviceSerial: String?,
    @SerializedName("person") val person: Owner?
) : Parcelable

@Parcelize
data class Owner(@SerializedName("name") val name: String, @SerializedName("surname") val surname: String) : Parcelable

@Parcelize
data class Token(@SerializedName("token") val token: String) : Parcelable
