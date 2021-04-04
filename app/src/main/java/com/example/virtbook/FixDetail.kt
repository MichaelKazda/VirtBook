package com.example.virtbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.virtbook.services.GraphicsMaker
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class FixDetail : AppCompatActivity() {
    private val graphicsMaker = GraphicsMaker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fix_detail)

        val fixPrice = intent.getLongExtra("fixPrice", 0)
        val fixKm = intent.getLongExtra("fixKm", 0)
        val fixDate = intent.getLongExtra("fixDate", 0)


        /*BINDING DATA TO FRONTEND*/
        findViewById<TextView>(R.id.fixArea)?.text = intent.getStringExtra("fixArea")
        findViewById<TextView>(R.id.fixName)?.text = intent.getStringExtra("fixName")
        findViewById<TextView>(R.id.fixDesc)?.text = intent.getStringExtra("fixDesc")
        findViewById<TextView>(R.id.fixPrice)?.text = graphicsMaker.spanIndex(fixPrice as Long,"Kč", resources.getDimensionPixelSize(R.dimen.big_index))
        findViewById<TextView>(R.id.fixKm)?.text = graphicsMaker.spanIndex(fixKm as Long,"km", resources.getDimensionPixelSize(R.dimen.big_index))
        findViewById<TextView>(R.id.fixDate)?.text = SimpleDateFormat("dd. MM. yyyy",  Locale.getDefault()).format(Timestamp(fixDate))
        findViewById<TextView>(R.id.fixAddress)?.text = intent.getStringExtra("fixAddress")
    }
}

