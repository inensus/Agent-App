package com.inensus.core_network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * A sellable appliance from the catalogue (`assets`), as served by
 * `GET app/agents/appliances/catalogue`.
 *
 * Agents used to sell from a stock assigned to them, where the row carried its own `cost`
 * and only an appliance type. They now sell from the same catalogue the web admin uses, so
 * an appliance has a name and a list price that the agent may override per sale.
 */
@Parcelize
data class Appliance(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: BigDecimal,
    @SerializedName("asset_type") val type: ApplianceType?
) : Parcelable

@Parcelize
data class ApplianceType(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
) : Parcelable
