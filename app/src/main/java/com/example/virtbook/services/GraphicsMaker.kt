package com.example.virtbook.services

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import java.lang.Math.pow
import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.pow

/**
 * Class for managing graphics
 */
class GraphicsMaker {

    // Converting timestamp to days
    fun tsToDaysConv(goalTS: Timestamp, toPositiveNum: Boolean = false): Long {
        val todayTS = System.currentTimeMillis()

        return if(toPositiveNum){
            TimeUnit.DAYS.convert((goalTS.time - todayTS), TimeUnit.MILLISECONDS).toDouble().pow(2.0).toLong()
        }else{
            TimeUnit.DAYS.convert((goalTS.time - todayTS), TimeUnit.MILLISECONDS)
        }
    }

    // Creating and setting graph
    fun createGraph(score: Int): Pie? {
        val graph = AnyChart.pie()

        //Graph visuals settings
        graph.palette(arrayOf("#000000", "#ffffff"))
        graph.innerRadius("85%")
        graph.legend().enabled(false)
        graph.title().enabled(false)
        graph.labels().enabled(false)
        graph.animation().enabled(false)
        graph.hovered().explode("0%")
        graph.hovered().outline().enabled(false)
        graph.selected().explode("0%")
        graph.selected().outline().enabled(false)
        graph.tooltip().enabled(false)
        graph.background().fill("#EAEAEA")

        //Graph values settings
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("", score)) // Score
        data.add(ValueDataEntry("", 100 - score)) // Rest of pie graph
        graph.data(data)

        return graph
    }

    // Spanning index and making it smaller
    fun spanIndex(value: Long, index: String, indexSize: Int): SpannableString {
        val string = "$value $index"
        val span = SpannableString(string)
        span.setSpan(AbsoluteSizeSpan(indexSize), string.length - index.length, string.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        return span
    }

    // Returns color of kmCount based on number of km until check required
    fun kmCountColor(kmCount: Long): String {
        return if (kmCount >= 2000){
            "#232323"
        }else if(kmCount >= 500){
            "#730000"
        }else if(kmCount >= 100){
            "#ab0202"
        }else{
            "#e30000"
        }
    }

    // Returns color of date based on distance from today
    fun dateColor(goalTS: Timestamp): String {
        val diffDays = tsToDaysConv(goalTS)

        if (diffDays >= 60){
            return "#232323"
        }else if(diffDays > 14){
            return "#730000"
        }else if(diffDays >= 7){
            return "#ab0202"
        }else{
            return "#e30000"
        }
    }
}