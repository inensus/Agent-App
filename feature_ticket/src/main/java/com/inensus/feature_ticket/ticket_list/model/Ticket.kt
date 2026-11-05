package com.inensus.feature_ticket.ticket_list.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Ticket(
    /**
     * The ticket's primary key. The backend also returns a `ticket_id` column, but that is a
     * leftover from when tickets lived in Trello and held the card id; locally created tickets
     * leave it empty, so navigating by it produced a detail URL with no id at all.
     */
    @SerializedName("id") val id: Long,
    @SerializedName("status") val status: Int,
    @SerializedName("category") val category: Category?,
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("created_at") val createdAt: Date
)

data class Category(@SerializedName("label_name") val name: String)
data class Owner(@SerializedName("name") val name: String, @SerializedName("surname") val surname: String)
