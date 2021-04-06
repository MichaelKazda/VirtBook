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
class CarCheckParrentAdapter(private var checkAreaList: List<String>, private var checkPointList: List<String>, private var  checkIconList: List<String>, private var  checkDescList: List<String>) : RecyclerView.Adapter<CarCheckParrentAdapter.ViewHolder>(){
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
        holder.checkArea.text = checkAreaList[position]
        val childLayoutManager = LinearLayoutManager(holder.childRecycler.context)
        holder.childRecycler.apply {
            layoutManager = childLayoutManager
            adapter = CarCheckChildAdapter(checkPointList, checkIconList, checkDescList)
            setRecycledViewPool(viewPool)
        }
    }

    // Returning items count
    override fun getItemCount(): Int {
        return checkAreaList.size
    }
}

/*class ParentAdapter(private val parents : List<ParentModel>) :    RecyclerView.Adapter<ParentAdapter.ViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.parent_recycler,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parents[position]
        holder.textView.text = parent.title
        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayout.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = ChildAdapter(parent.children)
            setRecycledViewPool(viewPool)
        }

    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_child
        val textView:TextView = itemView.textView
    }
}*/