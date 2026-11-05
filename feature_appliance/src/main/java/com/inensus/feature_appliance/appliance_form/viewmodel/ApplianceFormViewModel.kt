package com.inensus.feature_appliance.appliance_form.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_appliance.appliance_form.model.InstallmentMode
import com.inensus.feature_appliance.appliance_form.model.RateType
import com.inensus.feature_appliance.appliance_form.model.SellableDevice
import com.inensus.feature_appliance.appliance_form.repository.ApplianceFormRepository
import com.inensus.feature_appliance.appliance_form.view.ApplianceFormUiState
import com.inensus.feature_appliance.appliance_form.view.ApplianceFormValidator
import com.inensus.feature_appliance.appliance_form.view.ApplianceSummaryCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class ApplianceFormViewModel(
    private val repository: ApplianceFormRepository,
    private val summaryCreator: ApplianceSummaryCreator,
    private val validator: ApplianceFormValidator
) : BaseViewModel() {

    private val _uiState = LiveEvent<ApplianceFormUiState>()
    var uiState: LiveEvent<ApplianceFormUiState> = _uiState

    private val _appliance = MutableLiveData<Appliance>()
    val appliance: LiveData<Appliance> = _appliance

    private val _cost = MutableLiveData<BigDecimal>()
    val cost: LiveData<BigDecimal> = _cost

    private val _firstPaymentDate = MutableLiveData<Date>()
    val firstPaymentDate: LiveData<Date> = _firstPaymentDate

    private val _downPayment = MutableLiveData<BigDecimal>()
    val downPayment: LiveData<BigDecimal> = _downPayment

    private val _rateType = MutableLiveData(RateType.MONTHLY)
    val rateType: LiveData<RateType> = _rateType

    private val _mode = MutableLiveData(InstallmentMode.COUNT)
    val mode: LiveData<InstallmentMode> = _mode

    /** What the agent typed in count mode, and what is derived from the cost in cost mode. */
    private val _installmentCount = MutableLiveData<Int>()
    val installmentCount: LiveData<Int> = _installmentCount

    private val _installmentCost = MutableLiveData<BigDecimal>()
    val installmentCost: LiveData<BigDecimal> = _installmentCost

    /**
     * Units available for the selected appliance. Empty for appliances that are not tracked
     * one by one, which is also how the form knows not to ask for a device at all.
     */
    private val _devices = MutableLiveData<List<SellableDevice>>(emptyList())
    val devices: LiveData<List<SellableDevice>> = _devices

    private val _selectedDevice = MutableLiveData<SellableDevice?>()
    val selectedDevice: LiveData<SellableDevice?> = _selectedDevice

    /** What one installment works out to, shown under the form. */
    private val _installmentAmount = MutableLiveData(BigDecimal.ZERO)
    val installmentAmount: LiveData<BigDecimal> = _installmentAmount

    fun getAppliances() {
        if (repository.appliances.isEmpty()) {
            _uiState.postValue(ApplianceFormUiState.Loading)

            repository.getAppliancesTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _uiState.postValue(ApplianceFormUiState.AppliancesLoaded(it.data))
                }, {
                    _uiState.postValue(ApplianceFormUiState.Error)
                })
                .addTo(compositeDisposable)
        } else {
            _uiState.postValue(ApplianceFormUiState.AppliancesLoaded(repository.appliances))
        }
    }

    fun onApplianceChanged(applianceName: String) {
        val appliance = repository.appliances.firstOrNull { it.name == applianceName } ?: return

        _appliance.value = appliance
        // The catalogue price is a starting point; the agent may agree another one.
        _cost.value = appliance.price

        loadDevicesFor(appliance.id)
        recalculate()
    }

    private fun loadDevicesFor(assetId: Long) {
        _selectedDevice.value = null
        _devices.value = emptyList()

        repository.getSellableDevices(assetId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _devices.value = response.data
            }, {
                // Leaving the list empty keeps the device picker hidden rather than blocking
                // a sale that may not need one; validation still catches the case where the
                // appliance does require a unit.
                _devices.value = emptyList()
            })
            .addTo(compositeDisposable)
    }

    fun onDeviceChanged(serial: String) {
        _selectedDevice.value = _devices.value?.firstOrNull { it.serial == serial }
    }

    fun onCostChanged(cost: String) {
        _cost.value = cost.toBigDecimalOrNull()

        recalculate()
    }

    fun onFirstPaymentDateChanged(date: Date?) {
        _firstPaymentDate.value = date
    }

    fun onDownPaymentChanged(downPayment: String) {
        _downPayment.value = downPayment.toBigDecimalOrNull()

        recalculate()
    }

    fun onRateTypeChanged(label: String) {
        _rateType.value = RateType.fromLabel(label)
    }

    fun onModeChanged(mode: InstallmentMode) {
        if (_mode.value == mode) return

        _mode.value = mode

        recalculate()
    }

    fun onInstallmentCountChanged(count: String) {
        _installmentCount.value = count.toIntOrNull()

        recalculate()
    }

    fun onInstallmentCostChanged(cost: String) {
        _installmentCost.value = cost.toBigDecimalOrNull()

        recalculate()
    }

    private fun remainingCost(): BigDecimal {
        val cost = _cost.value ?: BigDecimal.ZERO
        val downPayment = _downPayment.value ?: BigDecimal.ZERO

        return (cost - downPayment).max(BigDecimal.ZERO)
    }

    /**
     * Keeps count, installment cost and the displayed amount consistent.
     *
     * Count mode is the agent's number of installments with the amount derived from it; cost
     * mode is the reverse. The arithmetic matches the backend's schedule, which floors every
     * installment and lets the last one absorb the rounding, so what the form shows is what
     * the customer will actually be billed.
     */
    private fun recalculate() {
        val remaining = remainingCost()

        if (remaining <= BigDecimal.ZERO) {
            // Paid in full up front: no schedule to build.
            _installmentCount.value = 0
            _installmentAmount.value = BigDecimal.ZERO
            return
        }

        when (_mode.value ?: InstallmentMode.COUNT) {
            InstallmentMode.COUNT -> {
                val count = _installmentCount.value ?: 0
                _installmentAmount.value =
                    if (count > 0) remaining.divide(count.toBigDecimal(), 0, RoundingMode.FLOOR) else BigDecimal.ZERO
            }
            InstallmentMode.COST -> {
                val installment = _installmentCost.value?.setScale(0, RoundingMode.FLOOR) ?: BigDecimal.ZERO

                if (installment <= BigDecimal.ZERO || installment > remaining) {
                    _installmentCount.value = 0
                    _installmentAmount.value = BigDecimal.ZERO
                } else {
                    val count = remaining.divide(installment, 0, RoundingMode.FLOOR).toInt()
                    _installmentCount.value = count
                    // What the agent typed only decides how many installments there are. The
                    // backend then spreads the remaining cost over that count, so showing the
                    // typed figure here would promise the customer a different bill.
                    _installmentAmount.value =
                        if (count > 0) remaining.divide(count.toBigDecimal(), 0, RoundingMode.FLOOR)
                        else BigDecimal.ZERO
                }
            }
        }
    }

    fun onContinueButtonTapped() {
        val errors = validator.validateForm(
            _appliance.value,
            _cost.value,
            _firstPaymentDate.value,
            _downPayment.value,
            _mode.value ?: InstallmentMode.COUNT,
            _installmentCount.value,
            _installmentCost.value,
            deviceRequired = _devices.value.orEmpty().isNotEmpty(),
            selectedDevice = _selectedDevice.value
        )

        if (errors.isNotEmpty()) {
            _uiState.value = ApplianceFormUiState.ValidationError(errors)
            return
        }

        val rateCount = _installmentCount.value ?: 0

        repository.appliance = _appliance.value
        repository.cost = _cost.value
        repository.downPayment = _downPayment.value
        repository.rateCount = rateCount
        repository.rateType = _rateType.value ?: RateType.MONTHLY
        repository.firstPaymentDate = _firstPaymentDate.value
        repository.deviceSerial = _selectedDevice.value?.serial

        _uiState.value = ApplianceFormUiState.OpenSummary(
            summaryCreator.createSummaryItems(
                appliance = _appliance.value!!,
                cost = _cost.value!!,
                firstPaymentDate = _firstPaymentDate.value!!,
                downPayment = _downPayment.value,
                rateType = _rateType.value ?: RateType.MONTHLY,
                rateCount = rateCount,
                installmentAmount = _installmentAmount.value ?: BigDecimal.ZERO
            )
        )
    }
}
