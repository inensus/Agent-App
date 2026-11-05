package com.inensus.feature_appliance.appliance_form.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.core.utils.AmountUtils
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.BaseActivity
import com.inensus.core_ui.extentions.compareWithoutTime
import com.inensus.core_ui.extentions.gone
import com.inensus.core_ui.extentions.show
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_form.model.InstallmentMode
import com.inensus.feature_appliance.appliance_form.model.RateType
import com.inensus.feature_appliance.appliance_form.viewmodel.ApplianceFormViewModel
import java.math.BigDecimal
import kotlinx.android.synthetic.main.fragment_appliance_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class ApplianceFormFragment : Fragment() {

    private val viewModel: ApplianceFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_appliance_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setListeners()
        observeUiState()

        viewModel.getAppliances()
    }

    private fun setupView() {
        with(cost.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        with(downPayment.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        with(tenure.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(LengthFilter(3))
        }

        with(installmentCost.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        rateTypeDropdown.bindData(rateTypeLabels())
        rateTypeDropdown.value = getString(R.string.appliance_rate_type_monthly)

        installmentModeDropdown.bindData(installmentModeLabels())
        installmentModeDropdown.value = getString(R.string.appliance_installment_mode_count)
    }

    private fun rateTypeLabels() = listOf(
        getString(R.string.appliance_rate_type_weekly),
        getString(R.string.appliance_rate_type_monthly)
    )

    private fun installmentModeLabels() = listOf(
        getString(R.string.appliance_installment_mode_count),
        getString(R.string.appliance_installment_mode_cost)
    )

    private fun setListeners() {
        view?.findViewById<Button>(R.id.btnRetry)?.setOnClickListener { viewModel.getAppliances() }

        applianceDropdown.onValueChanged = { viewModel.onApplianceChanged(it) }
        deviceDropdown.onValueChanged = { viewModel.onDeviceChanged(it) }
        paymentDate.onDateSelected = { viewModel.onFirstPaymentDateChanged(it) }

        cost.afterTextChanged = { viewModel.onCostChanged(it.toString()) }

        downPayment.afterTextChanged = {
            if (it.toString() == "0") {
                downPayment.setText("")
            } else {
                viewModel.onDownPaymentChanged(it.toString())
            }
        }

        rateTypeDropdown.onValueChanged = { label ->
            viewModel.onRateTypeChanged(
                if (label == getString(R.string.appliance_rate_type_weekly)) RateType.WEEKLY.apiValue
                else RateType.MONTHLY.apiValue
            )
        }

        installmentModeDropdown.onValueChanged = { label ->
            viewModel.onModeChanged(
                if (label == getString(R.string.appliance_installment_mode_cost)) InstallmentMode.COST
                else InstallmentMode.COUNT
            )
        }

        tenure.afterTextChanged = {
            if (it.toString() == "0") {
                tenure.setText("")
            } else {
                viewModel.onInstallmentCountChanged(it.toString())
            }
        }

        installmentCost.afterTextChanged = {
            if (it.toString() == "0") {
                installmentCost.setText("")
            } else {
                viewModel.onInstallmentCostChanged(it.toString())
            }
        }

        buttonContinue.setOnClickListener { viewModel.onContinueButtonTapped() }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApplianceFormUiState.Loading -> handleAppliancesLoadingState()
                is ApplianceFormUiState.AppliancesLoaded -> handleAppliancesLoadedState(it.appliances)
                is ApplianceFormUiState.Error -> handleErrorState()
                is ApplianceFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                is ApplianceFormUiState.ValidationError -> handleValidationError(it.errors)
            }
        })

        viewModel.appliance.observe(viewLifecycleOwner, Observer {
            if (applianceDropdown.value != it.name) {
                applianceDropdown.value = it.name
            }
        })

        viewModel.devices.observe(viewLifecycleOwner, Observer { devices ->
            // The picker exists only when the appliance actually has units to hand over, so
            // electronics keep the short form they had before.
            if (devices.isNullOrEmpty()) {
                deviceDropdown.gone()
            } else {
                deviceDropdown.bindData(devices.map { it.serial })
                deviceDropdown.show()
            }
        })

        viewModel.cost.observe(viewLifecycleOwner, Observer {
            if (it != null && cost.getMainTextView().text.toString() != it.toPlainString()) {
                cost.getMainTextView().setText(it.toPlainString())
            }
        })

        viewModel.firstPaymentDate.observe(viewLifecycleOwner, Observer {
            if (paymentDate.date?.compareWithoutTime(it) != 0) {
                paymentDate.date = it
            }
        })

        viewModel.downPayment.observe(viewLifecycleOwner, Observer {
            if (it != null && downPayment.getMainTextView().text.toString() != it.toPlainString()) {
                downPayment.getMainTextView().setText(it.toPlainString())
            }
        })

        viewModel.mode.observe(viewLifecycleOwner, Observer { mode ->
            // The two modes are the same sale typed in from opposite ends, so only the field
            // the agent drives is editable; the other one is shown as the derived result.
            if (mode == InstallmentMode.COST) {
                installmentCost.show()
                tenure.gone()
            } else {
                installmentCost.gone()
                tenure.show()
            }
        })

        // The derived installment count is deliberately not written back into the count
        // field: its text listener feeds the view model, and the "clear a lone zero" rule
        // below would bounce the two off each other forever. It is shown in the result line
        // instead, which is also where it is useful in cost mode.
        viewModel.installmentCount.observe(viewLifecycleOwner, Observer { renderInstallmentResult() })

        viewModel.installmentAmount.observe(viewLifecycleOwner, Observer { renderInstallmentResult() })
    }

    @SuppressLint("SetTextI18n")
    private fun renderInstallmentResult() {
        val count = viewModel.installmentCount.value ?: 0
        val amount = viewModel.installmentAmount.value ?: BigDecimal.ZERO

        amountValue.text =
            if (count > 0) "$count x ${AmountUtils.convertAmountToString(amount)}"
            else AmountUtils.convertAmountToString(amount)
    }

    private fun handleAppliancesLoadingState() {
        loadingLayout.show()
        errorLayout.gone()
        inputForm.gone()
    }

    private fun handleAppliancesLoadedState(appliances: List<Appliance>) {
        if (appliances.isEmpty()) {
            errorLayout.show()
            view?.findViewById<TextView>(R.id.tvErrorDescription)?.text =
                getString(R.string.appliance_catalogue_empty)
            inputForm.gone()
            loadingLayout.gone()
        } else {
            applianceDropdown.bindData(appliances.map { it.name })

            inputForm.show()
            errorLayout.gone()
            loadingLayout.gone()
        }
    }

    private fun handleErrorState() {
        errorLayout.show()
        inputForm.gone()
        loadingLayout.gone()
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue>) {
        (activity as BaseActivity).provideNavController()
            .navigate(R.id.openApplianceSummary, bundleOf(ApplianceSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<ApplianceFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is ApplianceFormUiState.ValidationError.Error.ApplianceIsBlank ->
                    applianceDropdown.setErrorState(true)
                is ApplianceFormUiState.ValidationError.Error.CostIsBlank -> {
                    Toast.makeText(context, getString(R.string.appliance_cost_error), Toast.LENGTH_LONG).show()
                    cost.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.FirstPaymentDateIsBlank ->
                    paymentDate.setErrorState(true)
                is ApplianceFormUiState.ValidationError.Error.DownPaymentMoreThanCost -> {
                    Toast.makeText(context, getString(R.string.appliance_down_payment_error), Toast.LENGTH_LONG).show()
                    downPayment.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.InstallmentCountIsBlank ->
                    tenure.setErrorState(true)
                is ApplianceFormUiState.ValidationError.Error.InstallmentCostIsBlank ->
                    installmentCost.setErrorState(true)
                is ApplianceFormUiState.ValidationError.Error.DeviceIsBlank -> {
                    Toast.makeText(context, getString(R.string.appliance_device_error), Toast.LENGTH_LONG).show()
                    deviceDropdown.setErrorState(true)
                }
                is ApplianceFormUiState.ValidationError.Error.InstallmentCostMoreThanRemaining -> {
                    Toast.makeText(context, getString(R.string.appliance_installment_cost_error), Toast.LENGTH_LONG)
                        .show()
                    installmentCost.setErrorState(true)
                }
            }
        }
    }
}
