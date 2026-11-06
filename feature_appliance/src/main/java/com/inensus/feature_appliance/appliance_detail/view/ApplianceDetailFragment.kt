package com.inensus.feature_appliance.appliance_detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.currency
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.core_ui.key_value.KeyValueAdapter
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_detail.viewmodel.ApplianceDetailViewModel
import kotlinx.android.synthetic.main.fragment_appliance_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class ApplianceDetailFragment : Fragment() {

    private val viewModel: ApplianceDetailViewModel by viewModel()
    private lateinit var applianceTransaction: ApplianceTransaction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_appliance_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applianceTransaction = arguments?.getParcelable(EXTRA_APPLIANCE)!!

        applianceTransaction.let {
            viewModel.getApplianceDetail(it)
        }

        setupListeners()
        observeApplianceDetails()
    }

    private fun observeApplianceDetails() {
        viewModel.applianceDetails.observe(viewLifecycleOwner, Observer {
            setupView(it)
        })

        viewModel.paymentDetails.observe(viewLifecycleOwner, Observer {
            paymentDetailsText.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setupView(applianceDetails: List<KeyValue>) {
        rvApplianceDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = KeyValueAdapter(applianceDetails)
        }

        viewModel.checkPaymentDetails(applianceTransaction)
    }

    private fun setupListeners() {
        paymentDetailsText.setOnClickListener {
            openPaymentDetails()
        }
    }

    private fun openPaymentDetails() {
        val keyValueList = applianceTransaction.rates.map {
            //TODO Change when server sends dueDate as Date instead of String
            KeyValue.Default(
                "${getString(R.string.appliance_detail_payments_due_date)} ${DateUtils.convertDateToString(
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).parse(it.dueDate) ?: Date()
                )}",
                if (it.remainingAmount == BigDecimal.ZERO) "${getString(R.string.appliance_detail_payments_paid)} ${it.rateAmount} ${requireContext().currency()}" else "${getString(R.string.appliance_detail_payments_remaining)} ${it.remainingAmount} ${requireContext().currency()}",
                if (it.remainingAmount == BigDecimal.ZERO) R.color.green_00C853 else R.color.gray_424242
            )
        }

        ApplianceDetailPaymentsFragment.newInstance(keyValueList).also {
            it.show(childFragmentManager, PAYMENT_DETAILS_FRAGMENT_TAG)
        }
    }

    companion object {
        const val EXTRA_APPLIANCE = "appliance"
        const val PAYMENT_DETAILS_FRAGMENT_TAG = "paymentDetailsFragmentTag"
    }
}
