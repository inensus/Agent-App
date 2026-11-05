package com.inensus.feature_appliance.appliance_form.model

import com.google.gson.annotations.SerializedName

/**
 * A physical unit that can be handed over with an appliance.
 *
 * Solar home systems and e-bikes are tracked one by one, so selling one means binding a
 * specific unused device to the customer. Appliances without such units return no devices.
 */
data class SellableDevice(
    @SerializedName("id") val id: Long,
    @SerializedName("device_serial") val serial: String,
    @SerializedName("device_type") val deviceType: String?
)

data class SellableDeviceResponse(@SerializedName("data") val data: List<SellableDevice>)
