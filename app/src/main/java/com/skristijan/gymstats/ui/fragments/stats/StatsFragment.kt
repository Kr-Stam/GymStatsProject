package com.skristijan.gymstats.ui.fragments.stats

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.animation.Easing.EaseInOutQuad
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ViewPortHandler
import com.skristijan.gymstats.R
import com.skristijan.gymstats.databinding.FragmentStatsBinding
import com.skristijan.gymstats.ui.fragments.stats.statDetails.StatDetailsActivity
import com.skristijan.gymstats.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StatsFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = StatsFragment()
    }

    private val viewModel: StatsViewModel by viewModels()

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var radarChart: RadarChart
    private lateinit var statRV: RecyclerView

    //Chart injections
    @Inject
    lateinit var dateValueFormatter: DateValueFormatter

    @Inject
    lateinit var blankValueFormatter: BlankValueFormatter

    @Inject
    lateinit var highlightMarker: HighlightMarker

    @Inject
    lateinit var weightValueFormatter: WeightValueFormatter

    @Inject
    lateinit var spiderHighlight: SpiderHighlightMarker

    private var dataValues: ArrayList<RadarEntry> = arrayListOf()
    private val radarDataSet = RadarDataSet(dataValues, "Data Set 1")
    private val radarData = RadarData(listOf(radarDataSet))
    private val spiderFormatter = PRSpider()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        initComponents()
//        viewModel.getAllPRs().observe(viewLifecycleOwner, Observer {
//            lineDataSet.clear()
//            lineData.clearValues()
//            it.forEach { pr ->
////                dataValues.add(Entry(pr.date.toFloat(), pr.weight))
//                Log.d("TEST", pr.type + " " + pr.weight)
//                lineDataSet.addEntryOrdered(Entry(pr.date.toFloat(), pr.weight))
//            }
//            lineData.addDataSet(lineDataSet)
//            lineChart.data = lineData
//            lineChart.invalidate()
//        })
        return binding.root
    }

    private fun initComponents() {
        initRecyclerView()
//        initChart()
    }

    private fun initRecyclerView() {
        statRV = binding.statsRV
        val adapter = StatsAdapter(mutableListOf("Weight", "Bench", "Deadlift"))
        adapter.setOnStatClickListener {
            val intent = Intent(context, StatDetailsActivity::class.java)
            intent.putExtra("prName", it)
            startActivity(intent)
        }
        statRV.adapter = adapter
//        statRV.visibility = View.GONE
    }

//    private fun initChart() {
//        radarChart = binding.radarChart
//        dataValues = arrayListOf<RadarEntry>()
//        dataValues.add(RadarEntry(20f))
//        dataValues.add(RadarEntry(70f))
//        dataValues.add(RadarEntry(50f))
//        dataValues.add(RadarEntry(60f))
//        dataValues.add(RadarEntry(30f))
//        radarDataSet.clear()
//        dataValues.forEach{
//            radarDataSet.addEntry(it)
//        }
//        styleChart()
//        radarChart.data = radarData
//        radarChart.marker = spiderHighlight
//        radarChart.invalidate()
//    }
//
//    private fun styleChart() {
//        //Color Styling
//        radarDataSet.setDrawFilled(true)
//        radarDataSet.fillAlpha = 100
//        radarDataSet.color = ContextCompat.getColor(requireContext(), R.color.design_default_color_primary_dark)
//        radarDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.design_default_color_primary)
//        radarDataSet.valueFormatter = spiderFormatter
//        radarData.setDrawValues(false)
//
//        //Highlight Styling
//        radarDataSet.setDrawHighlightIndicators(false)
//        radarDataSet.highlightCircleOuterRadius = 3f
//        radarDataSet.highlightCircleInnerRadius = 0f
//        radarDataSet.isDrawHighlightCircleEnabled = true
//        radarDataSet.highlightCircleFillColor = ContextCompat.getColor(requireContext(), R.color.design_default_color_primary)
//
//        val xAxis = radarChart.xAxis
//        xAxis.xOffset = 0f
//        xAxis.yOffset = 0f
////        xAxis.setTypeface(mTfLight)
//        xAxis.textSize = 12f
//        xAxis.valueFormatter = spiderFormatter
//        xAxis.enableGridDashedLine(5f, 2f, 1f)
//
//
//        val yAxis = radarChart.yAxis
//        yAxis.axisMinimum = 0f
//        yAxis.axisMaximum = 50f
//        yAxis.textSize = 9f
//        yAxis.setLabelCount(5, false)
//        yAxis.setDrawLabels(false)
//
//        radarChart.legend.isEnabled = false
//        radarChart.description.isEnabled = false
//        //Alternate animation
////        radarChart.animateXY(
////            1400, 1400,
////            Easing.EaseInOutQuad,
////            Easing.EaseInOutQuad
////        )
//        radarChart.animateY(
//            2000,
//            EaseInOutQuad
//        )
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        // Remove the LifecycleObserver once you get a callback to ON_CREATE
        activity?.lifecycle?.removeObserver(this)

        // Then do your logic that specifically needs to wait for the Activity
        // to be created
        Log.d("callback", "lifecycle observer is called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class PRSpider : ValueFormatter(){

        private val mFactors =  arrayListOf("Bench", "Overhead", "Deadlift", "Squat", "Row")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return mFactors[value.toInt() % mFactors.size]
        }
    }
}