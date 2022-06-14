package com.skristijan.gymstats.util

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.skristijan.gymstats.R
import java.text.SimpleDateFormat
import java.util.*


class WeightValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString() + "kg"
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return value.toInt().toString() + "kg"
    }
}

class DateValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return SimpleDateFormat("MMM YY").format(Date(value.toLong()))
    }
}

class BlankValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        var result = ""
        var i = 1;
        while(i < (value.toString().length) + " kg".length){
            result +=  " "
            i++
        }
        return result
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        var result = ""
        var i = 1;
        while(i < (value.toString().length) + " kg".length){
            result +=  " "
            i++
        }
        return result
    }
}

class HighlightMarker(context: Context, layoutResource: Int): MarkerView(
    context,
    layoutResource
){
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val valueTextView: TextView = findViewById(R.id.valueTextView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        valueTextView.text = e?.y.toString() + " kg"
        dateTextView.text = SimpleDateFormat("DD MM YYYY").format(Date(e!!.x.toLong()))
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}

class SpiderHighlightMarker(context: Context, layoutResource: Int): MarkerView(
    context,
    layoutResource
){
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val valueTextView: TextView = findViewById(R.id.valueTextView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        var result = ""
        when(e?.y?.toInt()?.div(10)){
            0 -> result = "F"
            1 -> result = "D"
            2 -> result = "C"
            3 -> result = "B"
            4 -> result = "A"
            5 -> result = "S"
            6 -> result = "SSS"
            7 -> result = "SSS"
        }
        valueTextView.text = result
        valueTextView.setPadding(0, 10, 0, 0)
        valueTextView.textSize = 15f
        dateTextView.visibility = GONE
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}