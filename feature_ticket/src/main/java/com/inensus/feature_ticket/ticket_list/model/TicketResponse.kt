package com.inensus.feature_ticket.ticket_list.model

import com.google.gson.annotations.SerializedName

data class TicketResponse(
    @SerializedName("next_page_url") val nextPageUrl: String?,
    @SerializedName("data") val data: List<Ticket>
)