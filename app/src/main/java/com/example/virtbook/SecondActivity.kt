package com.example.virtbook

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.virtbook.services.GraphicsMaker
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SecondActivity : AppCompatActivity() {
    private val graphicsMaker = GraphicsMaker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Parsing JSON and generating views in Linear layout
        val json = intent.getStringExtra("data")
        val data: Map<String, Any> = Gson().fromJson(
            json, object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )

        // TODO naládovat data do grafické šablony a vložit do listview
        val out = StringBuilder()
        val checklists = data["productChecklist"] as List<*>
        var errorsCount = 0  // TODO spočítat celkový počet nálezů - inputType checkbox bool
        var x = 1
        while(x <= checklists.size - 1){
            val checkArea = checklists[x] as LinkedTreeMap<*,*>
            val checkAreaChecklist = checkArea["checkAreaChecklist"] as ArrayList<*>
            for(checkPoint in checkAreaChecklist){
                checkPoint as LinkedTreeMap<*,*>
                // Dividing data based on their input type
                if(checkPoint["inputType"] == "checkBox"){
                    val value = checkPoint["value"] as LinkedTreeMap<*,*>
                    out.append(value["bool"].toString() + " " + value["note"].toString() + "\n")
                }else if(checkPoint["inputType"] == "datePickerYear"){
                    out.append(SimpleDateFormat("dd. MM. yyyy",  Locale.getDefault()).format(checkPoint["value"]) + "\n")
                }else{
                    out.append(checkPoint["value"].toString())
                }
            }
            x++
        }
        Log.e("EPICCCC", out.toString())
    }
}