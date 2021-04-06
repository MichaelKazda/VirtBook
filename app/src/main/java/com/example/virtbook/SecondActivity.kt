package com.example.virtbook

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.virtbook.services.GraphicsMaker
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SecondActivity : AppCompatActivity() {
    private val graphicsMaker = GraphicsMaker()

    private var checkList = mutableListOf<HashMap<*,*>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Parsing JSON and getting data from intent
        val json = intent.getStringExtra("carCheckData")
        val errorsTotal = intent.getStringExtra("errorsTotal")?.toLong()
        val fixPrice = intent.getStringExtra("fixPrice")?.toLong()
        val data: Map<String, Any> = Gson().fromJson(
            json, object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )

        // Dividing parsed json data into lists
        val checklists = data["productChecklist"] as List<*>
        var x = 1
        while(x <= checklists.size - 1){
            val checkArea = checklists[x] as LinkedTreeMap<*,*>
            val checkAreaChecklist = checkArea["checkAreaChecklist"] as ArrayList<*>
            val checkAreaName = checkArea["checkAreaName"].toString()
            val checkPointList = mutableListOf<String>()
            val checkIconList = mutableListOf<String>()
            val checkDescList = mutableListOf<String>()
            for(checkPoint in checkAreaChecklist){
                checkPoint as LinkedTreeMap<*,*>
                val value = checkPoint["value"] as LinkedTreeMap<*,*>
                checkPointList.add(checkPoint["label"].toString())
                checkIconList.add(value["bool"].toString())
                checkDescList.add(value["note"].toString())
            }
            checkList.add(
                    hashMapOf(
                        "checkArea" to checkAreaName,
                        "checkPoint" to checkPointList,
                        "checkIcon" to checkIconList,
                        "checkDesc" to checkDescList
                    )
            )
            x++
        }

        /*BINDING DATA TO FRONTEND*/
        // Filling recycler view with data
        findViewById<RecyclerView>(R.id.carcheckRecyclerMain).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.carcheckRecyclerMain).adapter = CarCheckParrentAdapter(checkList)

        // Stats section
        findViewById<TextView>(R.id.fixCount).text = graphicsMaker.spanIndex(errorsTotal as Long,"nálezů", resources.getDimensionPixelSize(R.dimen.big_index)) // Total errors found stat
        findViewById<TextView>(R.id.fixCosts).text = graphicsMaker.spanIndex(fixPrice as Long,"Kč", resources.getDimensionPixelSize(R.dimen.big_index)) // Costs for errors fixes
    }
}