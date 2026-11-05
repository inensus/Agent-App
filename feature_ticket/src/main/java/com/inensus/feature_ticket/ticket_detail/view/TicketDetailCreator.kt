package com.inensus.feature_ticket.ticket_detail.view

import android.content.Context
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.DateUtils.DATE_FORMAT_FULL
import com.inensus.core_ui.key_value.KeyValue
import com.inensus.feature_ticket.R
import com.inensus.feature_ticket.ticket_detail.model.TicketDetail
import java.util.*

class TicketDetailCreator(private val context: Context) {

    fun createDetailItems(ticketDetail: TicketDetail) = arrayListOf(
        KeyValue.Default(context.getString(R.string.ticket_detail_id), ticketDetail.id.toString()),
        KeyValue.Default(context.getString(R.string.ticket_detail_description), ticketDetail.content ?: ""),
        KeyValue.Default(context.getString(R.string.ticket_detail_category), ticketDetail.category?.name ?: ""),
        KeyValue.Default(
            context.getString(R.string.ticket_detail_owner),
            context.getString(
                R.string.customer_name_surname,
                ticketDetail.owner?.name?.toUpperCase(Locale.getDefault()),
                ticketDetail.owner?.surname?.toUpperCase(Locale.getDefault())
            )
        ),
        KeyValue.Default(
            context.getString(R.string.ticket_detail_status),
            when (ticketDetail.status) {
                1 -> context.getString(R.string.ticket_detail_status_closed)
                0 -> context.getString(R.string.ticket_detail_status_open)
                else -> context.getString(R.string.ticket_detail_status_open)
            },
            when (ticketDetail.status) {
                1 -> R.color.green_00C853
                0 -> R.color.yellow_e7b53f
                else -> R.color.yellow_e7b53f
            }
        ),
        KeyValue.Default(context.getString(R.string.ticket_detail_date), DateUtils.convertDateToString(ticketDetail.createdAt, DATE_FORMAT_FULL))
    )
}
