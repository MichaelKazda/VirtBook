
package com.example.virtbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChart.sunburst
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Creation and settings of score graph
        val graph = AnyChart.pie()
        graph.palette(arrayOf("#000000", "#ffffff"))
        graph.innerRadius("80%")
        graph.legend().enabled(false)
        graph.title().enabled(false)
        graph.labels().enabled(false)
        graph.animation().enabled(false)
        graph.hovered().explode("0%")
        graph.hovered().outline().enabled(false)
        graph.selected().explode("0%")
        graph.selected().outline().enabled(false)
        graph.tooltip().enabled(false)

        //Setting graph values
        val score = 56
        view.findViewById<TextView>(R.id.scoreNumber).text = score.toString() + "%"
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("", score)) // Score
        data.add(ValueDataEntry("", 100 - score)) // Rest of pie graph
        graph.data(data)

        //Creating graph
        val anyChartView = view.findViewById<View>(R.id.scoreGraph) as AnyChartView
        anyChartView.setChart(graph)

        /*view.findViewById<Button>(R.id.button_first).setOnClickListener {
            var checksData = ""
            db.collection("checks").get().addOnSuccessListener { result ->
                for (document in result){
                    val price = document.data["price"]
                    val kmCount = document.data["kmCount"]
                    val checkArea = document.data["checkArea"]
                    checksData += "Cena -> $price Kč, Aktuálně najeto -> $kmCount km, Opraveno -> $checkArea \n"
                    view.findViewById<TextView>(R.id.textview_first).text = checksData
                }
            }
        }*/
    }
}