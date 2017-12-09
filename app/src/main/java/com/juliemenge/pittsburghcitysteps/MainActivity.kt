package com.juliemenge.pittsburghcitysteps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import kotlin.collections.ArrayList
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.recyclerview_item_row.*
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.toast
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    //neighborhoods to choose from for spinner
    private val neighborhoods = arrayOf("Allentown", "Arlington", "Banksville", "Bedford Dwellings", "Beechview", "Beltzhoover", "Bloomfield", "Bluff", "Bon Air", "Brighton Heights", "Brookline", "California-Kirkbride", "Carrick", "Central Lawrenceville", "Central Northside", "Central Oakland", "Chartiers City", "Crafton Heights", "Crawford-Roberts", "Duquesne Heights", "East Allegheny", "East Carnegie", "East Hills", "Elliott", "Esplen", "Fineview", "Garfield", "Glen Hazel", "Greenfield", "Hazelwood", "Highland Park", "Homewood North", "Knoxville", "Larimer", "Lincoln-Lemington-Belmar", "Lincoln Place", "Lower Lawrenceville", "Marhsall-Shadeland", "Middle Hill", "Morningside", "Mount Oliver Borough", "Mount Washington", "Mt. Oliver", "North Oakland", "Oakwood", "Overbrook", "Perry North", "Perry South", "Point Breeze", "Polish Hill", "Ridgemont", "Shadyside", "Sheraden", "South Oakland", "South Side Flats", "South Side Slopes", "Spring Garden", "Spring Hill-City View", "Squirrel Hill North", "Squirrel Hill South", "Stanton Heights", "St. Clair", "Strip District", "Terrace Village", "Troy Hill", "Upper Hill", "Upper Lawrenceville", "West End", "West Oakland", "Westwood", "Windgap")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: add onclicklistener to display images
        //TODO: how to track steps visited, number of actual steps

        //spinner to select neighborhood
        val aa: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, neighborhoods)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        neighborhoodSpinner.adapter = aa

        //display list of steps based on chosen neighborhood
        neighborhoodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                // Review: Use string templates
                val neighborhood = "'${neighborhoodSpinner.selectedItem}'"

                async(UI) {
                    val network = NetworkApi()
                    val data = bg { network.getDataFromServer(neighborhood) } //make network call and pass in user selected neighborhood
                    val stepList = data.await()

                    //set up the recyclerview to display list of steps

                    //val adapter = RecyclerAdapter(stepList)
                    //used this when there was no clicking

                    val adapter = RecyclerAdapter(stepList) {toast(it.name)} //what to do when the item is clicked - within the {}
                    //used this to click and display name of step

                    //val adapter = RecyclerAdapter(stepList) {alertDialogBuilder()} //what to do when the item is clicked - within the {}
                    //display alert dialog on click - need to pass in it.image, probably, to display image somehow, start just with url

                    recyclerView.adapter = adapter

                }
            }
        }

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    //alert dialog to hopefully display an image on click
    private fun alertDialogBuilder(): AlertDialogBuilder {
        return alert("Testing alerts") {
            title = "Alert"
            positiveButton("Cool") { toast("Yess!!!") }
            negativeButton("Never Ever") { }
            neutralButton("I'll think about it")
        }.show()

    }
}