package com.skristijan.gymstats.ui.fragments.stats.statDetails

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.skristijan.gymstats.R
import com.skristijan.gymstats.databinding.ActivityStatsDetailsBinding
import com.skristijan.gymstats.databinding.DialogEditPrBinding
import com.skristijan.gymstats.ui.fragments.stats.StatsAdapter
import com.skristijan.gymstats.ui.fragments.stats.StatsViewModel
import com.skristijan.gymstats.util.BlankValueFormatter
import com.skristijan.gymstats.util.DateValueFormatter
import com.skristijan.gymstats.util.HighlightMarker
import com.skristijan.gymstats.util.WeightValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatDetailsActivity : AppCompatActivity() {
    private val viewModel: StatsViewModel by viewModels()

    //Chart injections
    @Inject
    lateinit var dateValueFormatter: DateValueFormatter

    @Inject
    lateinit var blankValueFormatter: BlankValueFormatter

    @Inject
    lateinit var highlightMarker: HighlightMarker

    @Inject
    lateinit var weightValueFormatter: WeightValueFormatter

    private var _binding: ActivityStatsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart
    private lateinit var statRV: RecyclerView
    private lateinit var adapter: StatHistoryAdapter

    private lateinit var dialog: AlertDialog
    private lateinit var dialogView: View

    private val dataValues: ArrayList<Entry> = arrayListOf()
    private val lineDataSet = LineDataSet(dataValues, "Data Set 1")
    private val lineData = LineData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStatsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents() {
        initAlertDialog()
        initRecyclerView()
        initChart()
    }

    private fun initAlertDialog() {
        dialog = AlertDialog.Builder(this).create()
        dialog.setCancelable(true)
        dialog.setTitle("Title")
//            dialog.setMessage("Message")
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_pr, null)
        dialog.setView(dialogView)
    }

    private fun initRecyclerView() {
        statRV = binding.statHistoryRV
        adapter = StatHistoryAdapter(mutableListOf())
        statRV.adapter = adapter
        adapter.setOnHistoryTappedListener { pr, view ->
            Toast.makeText(this, pr.weight.toString(), Toast.LENGTH_LONG).show()
            dialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "UPDATE",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    viewModel.update(pr)
                    view.setBackgroundColor(Color.WHITE)
                })
            dialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                "DELETE",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    viewModel.delete(pr)
                    view.setBackgroundColor(Color.WHITE)
                })
            dialog.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                "CANCEL",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    view.setBackgroundColor(Color.WHITE)
                })
            dialog.setOnDismissListener {
                view.setBackgroundColor(Color.WHITE)
            }
            dialogView.findViewById<EditText>(R.id.valueET)?.hint = pr.weight.toString()
            dialog.show()
        }
    }

    private fun initChart() {
        lineChart = binding.lineChart
        styleChart()
        fetchChartData()
        lineChart.invalidate()
    }

    private fun fetchChartData() {
        var type = intent.getStringExtra("prName") ?: ""
        type = type.toLowerCase()
        viewModel.getPRsByType(type).observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.swapData(it)
                lineDataSet.clear()
                lineData.clearValues()
                it.forEach { pr ->
//                dataValues.add(Entry(pr.date.toFloat(), pr.weight))
                    Log.d("TEST", pr.type + " " + pr.weight)
                    lineDataSet.addEntryOrdered(Entry(pr.date.toFloat(), pr.weight))
                }
                lineData.addDataSet(lineDataSet)
                lineChart.data = lineData
                lineChart.invalidate()
            }
        })
    }

    private fun styleChart() {
        //Line styling
        lineDataSet.lineWidth = 3f
        lineDataSet.color = ContextCompat.getColor(
            this,
            R.color.design_default_color_primary
        )
        lineDataSet.setDrawCircleHole(true)
        lineDataSet.setDrawValues(true)
        lineDataSet.valueTextSize = 10f
        lineDataSet.circleRadius = 5f
        lineDataSet.circleHoleRadius = 2f
        lineDataSet.valueFormatter = weightValueFormatter
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleColors = listOf(
            ContextCompat.getColor(
                this,
                R.color.design_default_color_primary_dark
            )
        )
        lineDataSet.setDrawValues(false)
        //Highlight Styling
        lineDataSet.highLightColor = ContextCompat.getColor(
            this,
            R.color.design_default_color_primary
        )
        lineDataSet.highlightLineWidth = 2f
        lineDataSet.enableDashedHighlightLine(5f, 10f, 0.5f)
        lineChart.marker = highlightMarker
        //Chart styling
        lineChart.setNoDataText("NO DATA AVAILABLE")
        lineChart.description.isEnabled = false
        lineChart.setNoDataTextColor(
            ContextCompat.getColor(
                this,
                R.color.design_default_color_primary_dark
            )
        )
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        //Grid Lines
        lineChart.axisLeft.setDrawGridLines(true)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(true)

        lineChart.axisRight.setDrawLabels(true)
        lineChart.axisLeft.setDrawZeroLine(true)
        lineChart.axisRight.setDrawAxisLine(true)
        lineChart.axisLeft.setDrawAxisLine(true)
        lineChart.xAxis.setDrawAxisLine(true)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        //TODO DECIDE IF THIS SHOULD GO
        lineChart.xAxis.setLabelCount(3, true)
        //Touch controls
        lineChart.isDragEnabled = true

        //Legend
        val legend = lineChart.legend
        legend.isEnabled = false
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 10f
        //Value Formatter for Date
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = dateValueFormatter
        lineChart.axisLeft.valueFormatter = weightValueFormatter
        lineChart.axisRight.valueFormatter = blankValueFormatter
        //
//        lineChart.setExtraOffsets(0f,0f,7f,0f)
//        lineChart.extraRightOffset = lineChart.extraRightOffset
//        lineChart.offsetLeftAndRight(10)
    }
}