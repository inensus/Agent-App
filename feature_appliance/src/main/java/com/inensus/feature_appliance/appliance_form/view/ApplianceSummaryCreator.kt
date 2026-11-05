package com.inensus.feature_appliance.appliance_form.view

import android.content.Context
import com.inensus.core.utils.AmountUtils.convertAmountToString
import com.inensus.core.utils.DateUtils
import com.inensus.core_network.model.Appliance
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.R
import com.inensus.feature_appliance.appliance_form.model.RateType
import java.math.BigDecimal
import java.util.*

class ApplianceSummaryCreator(private val context: Context) {

    fun createSummaryItems(
        appliance: Appliance,
        cost: BigDecimal,
        firstPaymentDate: Date,
        downPayment: BigDecimal?,
        rateType: RateType,
        rateCount: Int,
        installmentAmount: BigDecimal
    ) = arrayListOf(
        KeyValue.Default(
            context.getString(R.string.appliance),
            appliance.name
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_cost),
            convertAmountToString(cost),
            context.getString(R.string.default_currency)
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_down_payment),
            convertAmountToString(downPayment ?: BigDecimal.ZERO),
            context.getString(R.string.default_currency)
        ),
        KeyValue.Default(
            context.getString(R.string.appliance_rate_type),
            context.getString(
                if (rateType == RateType.WEEKLY) R.string.appliance_rate_type_weekly
                else R.string.appliance_rate_type_monthly
            )
        ),
        KeyValue.Default(
            context.getString(R.string.appliance_installment_count),
            rateCount.toString()
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_installment_amount),
            convertAmountToString(installmentAmount),
            context.getString(R.string.default_currency)
        ),
        KeyValue.Default(
            context.getString(R.string.appliance_payment_date),
            DateUtils.convertDateToString(firstPaymentDate)
        )
    )
}
