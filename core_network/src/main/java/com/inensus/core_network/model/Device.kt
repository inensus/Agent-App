package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * A device owned by a customer. Replaces the former Meter/MeterGroup pair: the backend
 * no longer exposes a `meters` relation with a nested `meter.serial_number`, but a flat
 * `devices` list carrying `device_serial`.
 */
@Parcelize
data class Device(
    @SerializedName("id") val id: Int,
    @SerializedName("device_serial") val deviceSerial: String,
    @SerializedName("device_type") val deviceType: String?
) : Parcelable
