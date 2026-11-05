package com.inensus.feature_payment.payment_detail.view

import android.content.Context
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.DateUtils.DATE_FORMAT_FULL
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_payment.R
import com.inensus.feature_payment.payment_detail.model.PaymentDetail

class PaymentDetailCreator(private val context: Context) {

    fun createDetailItems(paymentDetail: PaymentDetail): List<KeyValue> {
        val list = arrayListOf(
            KeyValue.Default(context.getString(R.string.payment_detail_transaction_id), paymentDetail.transactionId.toString()),
            KeyValue.Default(
                context.getString(R.string.payment_detail_customer), context.getString(
                    R.string.customer_name_surname,
                    paymentDetail.paymentDevice?.person?.name,
                    paymentDetail.paymentDevice?.person?.surname
                )
            ),
            KeyValue.Amount(
                context.getString(R.string.payment_detail_amount),
                AmountUtils.convertAmountToString(paymentDetail.amount),
                context.getString(R.string.default_currency)
            ),
            KeyValue.Default(context.getString(R.string.payment_detail_token), paymentDetail.token?.token ?: ""),
            KeyValue.Default(context.getString(R.string.payment_detail_payment_type), paymentDetail.type),
            KeyValue.Default(context.getString(R.string.payment_detail_meter), paymentDetail.meter),
            KeyValue.Default(
                context.getString(R.string.payment_detail_date),
                DateUtils.convertDateToString(paymentDetail.createdAt, DATE_FORMAT_FULL)
            ),
            KeyValue.Default(
                context.getString(R.string.payment_detail_status),
                when (paymentDetail.originalTransaction?.status) {
                    1 -> context.getString(R.string.payment_detail_status_success)
                    0 -> context.getString(R.string.payment_detail_status_pending)
                    -1 -> context.getString(R.string.payment_detail_status_error)
                    else -> context.getString(R.string.payment_detail_status_success)
                },
                when (paymentDetail.originalTransaction?.status) {
                    1 -> R.color.green_00C853
                    0 -> R.color.yellow_e7b53f
                    -1 -> R.color.red_FF3B30
                    else -> R.color.green_00C853
                }
            )
        )

        if (paymentDetail.originalTransaction?.status == 1 && paymentDetail.sms != null) {
            list.add(KeyValue.Default(context.getString(R.string.payment_detail_sms_to), paymentDetail.sms.receiver))
            list.add(KeyValue.Default(context.getString(R.string.payment_detail_sms_body), paymentDetail.sms.body))
        }

        return list
    }
}
