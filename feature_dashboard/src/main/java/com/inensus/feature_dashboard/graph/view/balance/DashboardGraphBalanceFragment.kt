package com.inensus.feature_dashboard.graph.view.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.inensus.core_ui.graph.GraphMarkerView
import com.inensus.feature_dashboard.R
import com.inensus.feature_dashboard.graph.viewmodel.DashboardGraphBalanceViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_graph_balance.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DashboardGraphBalanceFragment : Fragment() {

    private val viewModel: DashboardGraphBalanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dashboard_graph_balance, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    private fun setupView() {
        setupChart()
    }

    private fun observeUiState() {
        viewModel.graphBalanceData.observe(viewLifecycleOwner, {
            when (it) {
                is DashboardGraphBalanceUiState.Success -> handleSuccessState(it.xAxisList, it.chartData)
            }
        })
    }

    private fun handleSuccessState(xAxisList: ArrayList<String>, chartData: List<List<Entry>>) {
        chart.xAxis.valueFormatter = DashboardGraphBalanceXAxisFormatter(xAxisList)
        setupChartData(chartData)
    }

    private fun setupChart() {
        chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)

            with(xAxis) {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                textColor = ContextCompat.getColor(context, R.color.gray_BDBDBD)
                textSize = 12f
                axisLineColor = ContextCompat.getColor(context, R.color.gray_EDEEF5)
                granularity = GRAPH_GRANULARITY
                isGranularityEnabled = true
                setDrawAxisLine(false)
            }

            with(axisLeft) {
                typeface = ResourcesCompat.getFont(chart.context, R.font.regular)
                setLabelCount(GRAPH_Y_ITEM_COUNT, false)
                typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
                textColor = ContextCompat.getColor(context, R.color.gray_BDBDBD)
                textSize = 12f
                setDrawAxisLine(false)
            }

            axisRight.isEnabled = false
            isScaleYEnabled = false

            marker = GraphMarkerView(chart.context)

            setExtraOffsets(
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET,
                GRAPH_OFFSET
            )

            setNoDataTextTypeface(ResourcesCompat.getFont(context, R.font.semi_bold))
            setNoDataTextColor(ContextCompat.getColor(context, R.color.gray_BDBDBD))
            setNoDataText(getString(R.string.dashboard_graph_empty_data))
        }
    }

    private fun setupChartData(chartData: List<List<Entry>>) {
        val dataSets = ArrayList<ILineDataSet>()

        chartData.forEachIndexed { index, list ->
            if (list.isNotEmpty()) {
                dataSets.add(generateDataSet(index, list))
            }
        }

        if (dataSets.isEmpty()) {
            chart.apply {
                data = null
                invalidate()
            }
        } else {
            chart.apply {
                data = LineData(dataSets)
                highlightValue(chartData.first().first().x, 0)
            }
        }
    }

    private fun generateDataSet(index: Int, entries: List<Entry>) =
        LineDataSet(entries, if (index == 0) getString(R.string.dashboard_graph_available_balance) else getString(R.string.dashboard_graph_due_balance)).apply {
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.gray_616161))
            circleHoleColor = ContextCompat.getColor(requireContext(), R.color.white)
            circleRadius = 3f
            circleHoleRadius = 1.7f
            lineWidth = GRAPH_LINE_WIDTH
            setDrawValues(false)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = if (index == 0) ContextCompat.getColor(requireContext(), R.color.colorPrimary) else ContextCompat.getColor(
                requireContext(),
                R.color.gray_616161
            )
        }

    companion object {
        private const val GRAPH_GRANULARITY = 1f
        private const val GRAPH_Y_ITEM_COUNT = 7
        private const val GRAPH_OFFSET = 16f
        private const val GRAPH_LINE_WIDTH = 3.2f
    }
}