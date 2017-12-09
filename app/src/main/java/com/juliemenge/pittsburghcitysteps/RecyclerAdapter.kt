package com.juliemenge.pittsburghcitysteps
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*




//class RecyclerAdapter(val stepList: List<Step>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
class RecyclerAdapter(val stepList: List<Step>,
        val itemClick: (Step) -> Unit) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        //return ViewHolder(v)
        return ViewHolder(v, itemClick)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(stepList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return stepList.size
    }

    //the class is holding the list view
    //class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class ViewHolder(itemView: View, val itemClick: (Step) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        fun bindItems(step: Step) {
            Picasso.with(itemView.context).load(step.image).into(itemView.stepPicture) //how to use this
            itemView.stepName.text = step.name
            itemView.stepNeighborhood.text = step.neighborhood
            itemView.stepMaterial.text = step.material
            itemView.stepLength.text = step.length.toString()
            itemView.stepImage.text = step.image
            itemView.setOnClickListener { itemClick(step) }
        }


    }

}
