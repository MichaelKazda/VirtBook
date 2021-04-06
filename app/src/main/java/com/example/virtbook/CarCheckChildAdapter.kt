package com.example.virtbook


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.virtbook.services.GraphicsMaker


/**
 * Class for creating adapter for recycler view - carCheck child adapter
 */
class CarCheckChildAdapter(private var checkPointList: List<String>, private var  checkIconList: List<String>, private var  checkDescList: List<String>) : RecyclerView.Adapter<CarCheckChildAdapter.ViewHolder>(){
    // Creating view holder for a recycler view item
    inner class ViewHolder(fixItemView: View): RecyclerView.ViewHolder(fixItemView){
        // Item content
        val checkPoint: TextView = fixItemView.findViewById(R.id.checkPoint)
        val checkDesc: TextView = fixItemView.findViewById(R.id.checkDesc)
        val checkIcon: AppCompatImageView = fixItemView.findViewById(R.id.checkIcon)
    }

    // Inflating an item from view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarCheckChildAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carcheck_child_item_layout, parent, false)
        return ViewHolder(view)
    }

    // Binding content to view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkPoint.text = checkPointList[position]
        holder.checkDesc.text = checkDescList[position]
        if(checkIconList[position] == "0"){
            holder.checkIcon.setImageResource(R.drawable.ic_baseline_check_24) // Check icon
        }else{
            holder.checkIcon.setImageResource(R.drawable.ic_baseline_close_24) // Cross icon
        }
    }

    // Returning items count
    override fun getItemCount(): Int {
        return checkPointList.size
    }
}