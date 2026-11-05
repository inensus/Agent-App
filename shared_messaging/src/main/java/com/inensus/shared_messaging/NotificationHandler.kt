package com.inensus.shared_messaging

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.inensus.core.utils.AmountUtils
import com.inensus.core_network.model.Payment
import com.inensus.core_ui.key_value.KeyValue
import java.math.BigDecimal

class NotificationHandler(private val context: Context) {

    private val gson = Gson()

    fun handleTransactionNotification(intent: Intent): List<KeyValue>? {
        val payment: Payment? = gson.fromJson(intent.getStringExtra(EXTRA_PAYLOAD), Payment::class.java)

        return createSummaryItems(
            payment?.device?.person?.name ?: "",
            payment?.device?.person?.surname ?: "",
            payment?.device?.deviceSerial ?: "",
            payment?.amount ?: BigDecimal.ZERO,
            payment?.token?.token ?: ""
        )
    }

    private fun createSummaryItems(name: String, surname: String, meter: String, amount: BigDecimal, token: String) =
        arrayListOf(
            KeyValue.Default(
                context.getString(R.string.payment_name_surname),
                "$name $surname"
            ),
            KeyValue.Default(
                context.getString(R.string.payment_meter),
                meter
            ),
            KeyValue.Amount(
                context.getString(R.string.payment_amount),
                AmountUtils.convertAmountToString(amount),
                context.getString(R.string.default_currency)
            ),
            KeyValue.Default(
                context.getString(R.string.payment_token),
                token
            )
        )

    companion object {
        const val EXTRA_PAYLOAD = "payload"
    }
}