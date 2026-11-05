package com.inensus.feature_appliance.appliance_list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core_network.model.ApplianceTransaction
import com.inensus.feature_appliance.R
import kotlinx.android.synthetic.main.appliance_list_item.view.*
import java.math.BigDecimal

class ApplianceListAdapter : RecyclerView.Adapter<ApplianceListAdapter.ViewHolder>() {

    lateinit var onItemClick: ((appliance: ApplianceTransaction) -> Unit)
    var appliances: List<ApplianceTransaction> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.appliance_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appliances[position])
    }

    override fun getItemCount() = appliances.size

    fun updateData(newItems: List<ApplianceTransaction>) {
        val diff = DiffUtil.calculateDiff(ApplianceDiffUtils(newItems, appliances))
        diff.dispatchUpdatesTo(this)
        appliances = newItems
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(transaction: ApplianceTransaction) {
            with(itemView) {
                statusImage.setImageDrawable(
                    if (transaction.rates.none { it.remainingAmount > BigDecimal.ZERO }) {
                        ContextCompat.getDrawable(context, R.drawable.ic_success)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_pending)
                    }
                )
                typeText.text = transaction.asset?.name ?: ""
                priceText.text = AmountUtils.convertAmountToString(transaction.cost)
                dateText.text = DateUtils.convertDateToString(transaction.createdAt)
                tenureValueText.text = transaction.tenure.toString()

                cardView.setOnClickListener {
                    onItemClick.invoke(transaction)
                }
            }
        }
    }
}