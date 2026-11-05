package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("addresses") val addresses: List<Address>,
    @SerializedName("devices") val devices: List<Device>?
): Parcelable
