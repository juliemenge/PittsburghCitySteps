package com.juliemenge.pittsburghcitysteps
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter(val stepList: ArrayList<Step>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(stepList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return stepList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(step: Step) {
            val textViewName = itemView.findViewById<TextView>(R.id.stepName)
            val textViewNeighborhood  = itemView.findViewById<TextView>(R.id.stepNeighborhood)
            val textViewMaterial  = itemView.findViewById<TextView>(R.id.stepMaterial)
            val textViewLength  = itemView.findViewById<TextView>(R.id.stepLength)
            textViewName.text = step.name
            textViewNeighborhood.text = step.neighborhood
            textViewMaterial.text = step.material
            textViewLength.text = step.length.toString()
        }
    }
}
