package com.inensus.feature_appliance

import com.inensus.feature_appliance.appliance_form.model.ConfirmApplianceRequest
import com.inensus.feature_appliance.appliance_form.model.SellableDeviceResponse
import com.inensus.feature_appliance.appliance_list.model.ApplianceResponse
import com.inensus.feature_appliance.appliance_list.model.ApplianceTypeResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApplianceService {
    @GET
    fun getAppliances(@Url url: String): Single<ApplianceResponse>

    @GET
    fun getApplianceTypes(@Url url: String): Single<ApplianceTypeResponse>

    @GET
    fun getSellableDevices(@Url url: String): Single<SellableDeviceResponse>

    @POST
    fun confirmAppliance(@Url url: String, @Body request: ConfirmApplianceRequest): Completable
}