package com.example.virtbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.virtbook.services.GraphicsMaker

class FixHistory : AppCompatActivity() {
    private var graphicsMaker = GraphicsMaker()

    private var fixNameList = mutableListOf<String>()
    private var fixAreaList = mutableListOf<String>()
    private var fixKmList = mutableListOf<Long>()
    private var fixDateList = mutableListOf<Long>()
    private var fixDescList = mutableListOf<String>()
    private var fixPriceList = mutableListOf<Long>()
    private var fixAddressList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fix_history)

        val fixHistory = intent.getStringArrayListExtra("fixHistory") as ArrayList<Map<String,*>>
        fixHistory.reverse() // Sort array by latest item first
        val costsTotal = intent.getStringExtra("costsTotal")?.toLong()
        val fixTotal = intent.getStringExtra("fixTotal")?.toLong()

        for (fixHistoryItem in fixHistory){
            fixNameList.add(fixHistoryItem["repairName"].toString())
            fixAreaList.add(fixHistoryItem["repairArea"].toString())
            fixDescList.add(fixHistoryItem["repairDesc"].toString())
            fixAddressList.add(fixHistoryItem["repairAddress"].toString())
            fixKmList.add(fixHistoryItem["repairKm"] as Long)
            fixDateList.add(fixHistoryItem["repairDate"] as Long)
            fixPriceList.add(fixHistoryItem["repairPrice"] as Long)
        }
        /*BINDING DATA TO FRONTEND*/
        // Filling recycler view with data
        findViewById<RecyclerView>(R.id.fixRecycler).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.fixRecycler).adapter = FixHistoryAdapter(fixNameList, fixAreaList, fixKmList, fixDateList, fixDescList, fixPriceList, fixAddressList)

        // Stats section
        findViewById<TextView>(R.id.fixCosts).text = graphicsMaker.spanIndex(costsTotal as Long,"Kč", resources.getDimensionPixelSize(R.dimen.big_index)) // Total errors found stat
        findViewById<TextView>(R.id.fixCount).text = graphicsMaker.spanIndex(fixTotal as Long,"úkonů", resources.getDimensionPixelSize(R.dimen.big_index)) // Costs for errors fixes
    }
}