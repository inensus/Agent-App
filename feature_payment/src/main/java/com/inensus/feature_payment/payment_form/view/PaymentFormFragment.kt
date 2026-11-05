package com.inensus.feature_payment.payment_form.view

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.inensus.core_ui.BaseActivity
import com.inensus.feature_payment.R
import com.inensus.feature_payment.payment_form.viewmodel.PaymentFormViewModel
import kotlinx.android.synthetic.main.fragment_payment_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.inensus.core_ui.key_value.KeyValue

@Suppress("UNCHECKED_CAST")
class PaymentFormFragment : Fragment() {

    private val viewModel: PaymentFormViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_payment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setListeners()
        observeUiState()
    }

    private fun setupView() {
        amount.getMainTextView().inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setListeners() {
        meterDropdown.onValueChanged = { viewModel.onMeterChanged(it) }
        amount.afterTextChanged = { viewModel.onAmountChanged(it.toString()) }
        buttonContinue.setOnClickListener {
            viewModel.onContinueButtonTapped()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PaymentFormUiState.OpenSummary -> openSummaryPage(it.summaryItems)
                is PaymentFormUiState.ValidationError -> handleValidationError(it.errors)
            }
        })

        viewModel.customer.observe(viewLifecycleOwner, Observer { customer ->
            customer?.devices?.let { devices ->
                meterDropdown.bindData(devices.map { it.deviceSerial })
            }
        })

        viewModel.meter.observe(viewLifecycleOwner, Observer {
            if (meterDropdown.value != it) {
                meterDropdown.value = it
            }
        })

        viewModel.amount.observe(viewLifecycleOwner, Observer {
            if (amount.getMainTextView().text.toString() != it) {
                amount.getMainTextView().setText(it.toString())
            }
        })
    }

    private fun openSummaryPage(summaryItems: ArrayList<KeyValue>) {
        (activity as BaseActivity).provideNavController()
            .navigate(R.id.openPaymentSummary, bundleOf(PaymentSummaryFragment.EXTRA_KEY_VALUE_LIST to summaryItems))
    }

    private fun handleValidationError(errors: List<PaymentFormUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is PaymentFormUiState.ValidationError.Error.MeterIsBlank -> {
                    meterDropdown.setErrorState(true)
                }
                is PaymentFormUiState.ValidationError.Error.AmountIsBlank -> {
                    amount.setErrorState(true)
                }
            }
        }
    }
}