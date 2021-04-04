package com.example.virtbook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.virtbook.services.GraphicsMaker
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class for creating adapter for recycler view
 */
class FixHistoryAdapter(private var fixNameList: List<String>, private var  fixAreaList: List<String>, private var  fixKmList: List<Long>, private var  fixDateList: List<Long>, private var fixDescList: List<String>, private var fixPriceList: List<Long>, private var fixAddressList: List<String>) : RecyclerView.Adapter<FixHistoryAdapter.ViewHolder>(){

    private val graphicsMaker = GraphicsMaker() // Handling graphics

    // Creating view holder for a recycler view item
    inner class ViewHolder(fixItemView: View): RecyclerView.ViewHolder(fixItemView){
        // Item content
        val fixName: TextView = fixItemView.findViewById(R.id.fixName)
        val fixArea: TextView = fixItemView.findViewById(R.id.fixArea)
        val fixKm: TextView = fixItemView.findViewById(R.id.fixKm)
        val fixDate: TextView = fixItemView.findViewById(R.id.fixDate)

        // Item on click event
        init {
            fixItemView.setOnClickListener{
                val intentFixDetail = Intent(fixItemView.context, FixDetail::class.java).apply {
                    // Passing data to new activity
                    putExtra("fixArea", fixAreaList[layoutPosition])
                    putExtra("fixName", fixNameList[layoutPosition])
                    putExtra("fixDesc", fixDescList[layoutPosition])
                    putExtra("fixPrice", fixPriceList[layoutPosition])
                    putExtra("fixKm", fixKmList[layoutPosition])
                    putExtra("fixDate", fixDateList[layoutPosition])
                    putExtra("fixAddress", fixAddressList[layoutPosition])
                }
                startActivity(fixItemView.context, intentFixDetail, null)
            }
        }
    }

    // Inflating an item from view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixHistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fix_item_layout, parent, false)
        return ViewHolder(view)
    }

    // Binding content to view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fixName.text = fixNameList[position]
        holder.fixArea.text = fixAreaList[position]
        holder.fixKm.text = graphicsMaker.spanIndex(fixKmList[position],"km", 45)
        holder.fixDate.text = SimpleDateFormat("dd. MM. yyyy",  Locale.getDefault()).format(Timestamp(fixDateList[position]))
    }

    // Returning items count
    override fun getItemCount(): Int {
        return fixNameList.size
    }
}