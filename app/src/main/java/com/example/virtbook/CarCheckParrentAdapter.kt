package com.example.virtbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Class for creating adapter for recycler view - carCheck parrent adapter
 */
class CarCheckParrentAdapter(private var checkList: List<HashMap<*,*>>) : RecyclerView.Adapter<CarCheckParrentAdapter.ViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()

    // Creating view holder for a recycler view item
    inner class ViewHolder(fixItemView: View): RecyclerView.ViewHolder(fixItemView){
        // Item content
        val checkArea: TextView = fixItemView.findViewById(R.id.checkArea)
        val childRecycler: RecyclerView = fixItemView.findViewById(R.id.carcheckRecyclerItem)
    }

    // Inflating an item from view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarCheckParrentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carcheck_parrent_item_layout, parent, false)
        return ViewHolder(view)
    }

    // Binding content to view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkHash = checkList[position]
        holder.checkArea.text = checkHash["checkArea"].toString()
        val childLayoutManager = LinearLayoutManager(holder.childRecycler.context)
        holder.childRecycler.apply {
            layoutManager = childLayoutManager
            adapter = CarCheckChildAdapter(checkHash["checkPoint"] as List<String>, checkHash["checkIcon"] as List<String>, checkHash["checkDesc"] as List<String>)
            setRecycledViewPool(viewPool)
        }
    }

    // Returning items count
    override fun getItemCount(): Int {
        return checkList.size
    }
}
