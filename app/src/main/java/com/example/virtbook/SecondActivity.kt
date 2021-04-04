package com.example.virtbook

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Parsing JSON and generating views in Linear layout
        val json = intent.getStringExtra("carCheckData")
        val errorsTotal = intent.getStringExtra("errorsTotal")?.toLong()
        val fixPrice = intent.getStringExtra("fixPrice")?.toLong()
        val data: Map<String, Any> = Gson().fromJson(
            json, object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )

        // TODO Opakovat proces jako u úkonů s vypsání do recycler view -> tentokrát dvojitý (vnitřní list check bodů a vnější list karet jednotlivých check sekcí)
        // TODO Dokumentace
        val out = StringBuilder()
        val checklists = data["productChecklist"] as List<*>
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

        /*BINDING DATA TO FRONTEND*/
        // Stats section
        findViewById<TextView>(R.id.fixCount).text = graphicsMaker.spanIndex(errorsTotal as Long,"nálezů", resources.getDimensionPixelSize(R.dimen.big_index)) // Total errors found stat
        findViewById<TextView>(R.id.fixCosts).text = graphicsMaker.spanIndex(fixPrice as Long,"Kč", resources.getDimensionPixelSize(R.dimen.big_index)) // Costs for errors fixes
    }
}