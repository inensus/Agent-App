package com.inensus.feature_appliance.appliance_detail.view

import android.content.Context
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.DateUtils.DATE_FORMAT_FULL
import com.inensus.core.utils.currency
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_appliance.R
import java.math.BigDecimal

class ApplianceDetailCreator(private val context: Context) {

    fun createDetailItems(transaction: ApplianceTransaction) = arrayListOf(
        KeyValue.Default(context.getString(R.string.appliance_detail_id), transaction.id.toString()),
        KeyValue.Default(context.getString(R.string.appliance_detail_asset), transaction.asset?.name ?: ""),
        KeyValue.Default(
            context.getString(R.string.appliance_detail_customer),
            context.getString(R.string.customer_name_surname, transaction.person.name, transaction.person.surname)
        ),
        KeyValue.Default(context.getString(R.string.appliance_detail_tenure), transaction.tenure.toString()),
        KeyValue.Default(
            context.getString(R.string.appliance_detail_date),
            DateUtils.convertDateToString(transaction.createdAt, DATE_FORMAT_FULL)
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_detail_paid_amount),
            AmountUtils.convertAmountToString(transaction.rates.filter { it.rateAmount - it.remainingAmount > BigDecimal.ZERO }
                .sumBy { (it.rateAmount - it.remainingAmount).intValueExact() }.toBigDecimal()),
            context.currency()
        ),
        KeyValue.Amount(
            context.getString(R.string.appliance_detail_remaining_amount),
            AmountUtils.convertAmountToString(transaction.rates.filter { it.remainingAmount > BigDecimal.ZERO }
                .sumBy { it.remainingAmount.intValueExact() }.toBigDecimal()),
            context.currency()
        ),
        KeyValue.Default(
            context.getString(R.string.payment_detail_status),
            if (transaction.rates.none { it.remainingAmount > BigDecimal.ZERO }) {
                context.getString(R.string.appliance_detail_completed)
            } else {
                context.getString(R.string.appliance_detail_ongoing)
            },
            if (transaction.rates.none { it.remainingAmount > BigDecimal.ZERO }) {
                R.color.green_00C853
            } else {
                R.color.yellow_e7b53f
            }
        )
    )
}
