package com.inensus.feature_dashboard.summary.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.DateUtils
import com.inensus.core.utils.currency
import com.inensus.feature_dashboard.R
import kotlinx.android.synthetic.main.dashboard_summary_item.view.*
import java.math.BigDecimal
import java.util.*

class DashboardSummaryItemView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private var iconResourceId: Int? = null
    private var title: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.dashboard_summary_item, this, true)
        initializeAttrs(context, attributeSet)
    }

    @SuppressLint("Recycle")
    private fun initializeAttrs(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.DashboardSummaryItemView).use {
            iconResourceId = it.getResourceId(R.styleable.DashboardSummaryItemView_summary_icon, 0)
            title = it.getString(R.styleable.DashboardSummaryItemView_summary_title)
        }
    }

    fun bindData(amount: BigDecimal, since: Date? = null) {
        tvAmountView.text = AmountUtils.convertAmountToString(amount)
        tvAmountCurrency.text = context.currency()

        tvDescriptionEnd.text = since?.let {
            context.getString(R.string.dashboard_summary_since, DateUtils.convertDateToString(it))
        } ?: let {
            context.getString(R.string.dashboard_summary_total)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        iconResourceId?.let { imageView.setImageDrawable(ContextCompat.getDrawable(context, it)) }
        title?.let { tvSubtitleDescription.text = it }
    }
}