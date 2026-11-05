package com.inensus.feature_appliance.appliance_form.view

import com.inensus.core_network.model.Appliance
import com.inensus.feature_appliance.appliance_form.model.InstallmentMode
import com.inensus.feature_appliance.appliance_form.model.SellableDevice
import java.math.BigDecimal
import java.util.*

class ApplianceFormValidator {
    fun validateForm(
        appliance: Appliance?,
        cost: BigDecimal?,
        firstPaymentDate: Date?,
        downPayment: BigDecimal?,
        mode: InstallmentMode,
        installmentCount: Int?,
        installmentCost: BigDecimal?,
        deviceRequired: Boolean,
        selectedDevice: SellableDevice?
    ): List<ApplianceFormUiState.ValidationError.Error> =
        mutableListOf<ApplianceFormUiState.ValidationError.Error>().apply {
            if (appliance == null) {
                add(ApplianceFormUiState.ValidationError.Error.ApplianceIsBlank)
            }
            if (cost == null || cost <= BigDecimal.ZERO) {
                add(ApplianceFormUiState.ValidationError.Error.CostIsBlank)
            }
            if (firstPaymentDate == null) {
                add(ApplianceFormUiState.ValidationError.Error.FirstPaymentDateIsBlank)
            }
            if (cost != null && downPayment != null && downPayment > cost) {
                add(ApplianceFormUiState.ValidationError.Error.DownPaymentMoreThanCost)
            }

            // An appliance that has units to hand over cannot be sold without naming one.
            if (deviceRequired && selectedDevice == null) {
                add(ApplianceFormUiState.ValidationError.Error.DeviceIsBlank)
            }

            val remaining = (cost ?: BigDecimal.ZERO) - (downPayment ?: BigDecimal.ZERO)

            when (mode) {
                InstallmentMode.COUNT ->
                    if (installmentCount == null || installmentCount <= 0) {
                        add(ApplianceFormUiState.ValidationError.Error.InstallmentCountIsBlank)
                    }
                InstallmentMode.COST -> when {
                    installmentCost == null || installmentCost <= BigDecimal.ZERO ->
                        add(ApplianceFormUiState.ValidationError.Error.InstallmentCostIsBlank)
                    installmentCost > remaining ->
                        add(ApplianceFormUiState.ValidationError.Error.InstallmentCostMoreThanRemaining)
                }
            }
        }
}
