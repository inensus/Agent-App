package com.inensus.feature_appliance.appliance_form.repository

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Appliance
import com.inensus.feature_appliance.ApplianceService
import com.inensus.feature_appliance.appliance_form.model.ConfirmApplianceRequest
import com.inensus.feature_appliance.appliance_form.model.RateType
import com.inensus.feature_appliance.appliance_form.model.SellableDevice
import com.inensus.shared_customer.repository.CustomerRepository
import java.math.BigDecimal
import java.util.*

class ApplianceFormRepository(
    private val service: ApplianceService,
    private val customerRepository: CustomerRepository,
    private val preferences: SharedPreferenceWrapper
) {
    var appliances: List<Appliance> = emptyList()
    var appliance: Appliance? = null
    var cost: BigDecimal? = null
    var downPayment: BigDecimal? = null
    var rateCount: Int? = null
    var rateType: RateType = RateType.MONTHLY
    var firstPaymentDate: Date? = null
    var deviceSerial: String? = null

    fun getAppliancesTypes() =
        service.getApplianceTypes(preferences.baseUrl + GET_APPLIANCE_CATALOGUE_ENDPOINT)
            .doOnSuccess { appliances = it.data }

    fun getSellableDevices(assetId: Long) =
        service.getSellableDevices(preferences.baseUrl + "app/agents/appliances/$assetId/devices")

    fun confirmAppliance() = service.confirmAppliance(
        preferences.baseUrl + CONFIRM_APPLIANCE_ENDPOINT,
        ConfirmApplianceRequest(
            customerRepository.customer?.id,
            appliance?.id,
            cost,
            downPayment ?: BigDecimal.ZERO,
            rateCount,
            rateType.apiValue,
            firstPaymentDate,
            deviceSerial
        )
    )

    companion object {
        private const val GET_APPLIANCE_CATALOGUE_ENDPOINT = "app/agents/appliances/catalogue"
        private const val CONFIRM_APPLIANCE_ENDPOINT = "app/agents/appliances"
    }
}
