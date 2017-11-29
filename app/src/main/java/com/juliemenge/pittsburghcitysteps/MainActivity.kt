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

class MainActivity : AppCompatActivity() {

    //neighborhoods to choose from for spinner
    val neighborhoods = arrayOf("Allentown", "Arlington", "Banksville", "Bedford Dwellings", "Beechview", "Beltzhoover", "Bloomfield", "Bluff", "Bon Air", "Brighton Heights", "Brookline", "California-Kirkbride", "Carrick", "Central Lawrenceville", "Central Northside", "Central Oakland", "Chartiers City", "Crafton Heights", "Crawford-Roberts", "Duquesne Heights", "East Allegheny", "East Carnegie", "East Hills", "Elliott", "Esplen", "Fineview", "Garfield", "Glen Hazel", "Greenfield", "Hazelwood", "Highland Park", "Homewood North", "Knoxville", "Larimer", "Lincoln-Lemington-Belmar", "Lincoln Place", "Lower Lawrenceville", "Marhsall-Shadeland", "Middle Hill", "Morningside", "Mount Oliver Borough", "Mount Washington", "Mt. Oliver", "North Oakland", "Oakwood", "Overbrook", "Perry North", "Perry South", "Point Breeze", "Polish Hill", "Ridgemont", "Shadyside", "Sheraden", "South Oakland", "South Side Flats", "South Side Slopes", "Spring Garden", "Spring Hill-City View", "Squirrel Hill North", "Squirrel Hill South", "Stanton Heights", "St. Clair", "Strip District", "Terrace Village", "Troy Hill", "Upper Hill", "Upper Lawrenceville", "West End", "West Oakland", "Westwood", "Windgap")

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
                val rawNeighborhoodChoice = neighborhoodSpinner.selectedItem.toString()
                val neighborhood = "'" + rawNeighborhoodChoice + "'"

                async(UI) {
                    val network = NetworkApi()
                    val data = bg { network.getDataFromServer(neighborhood) } //make network call and pass in user selected neighborhood
                    val useableData = data.await()

                    val allthedata = JSONObject(useableData) //get one giant blob of json data
                    val result = allthedata.getJSONObject("result") //create a json blob of just the result data
                    val records = result.getJSONArray("records") //get an array of the steps records

                    val arraySize = records.length() //size of the arraylist to store steps

                    val stepList = ArrayList<Step>(arraySize) //empty arraylist where steps will be stored

                    //iterate through json data and create arraylist of steps objects
                    for (i in 0..arraySize-1) {
                        val jsonStep = records.getJSONObject(i) //do stuff with the first step
                        val step = Step() //create a new step object
                        step.neighborhood = jsonStep.getString("neighborhood")
                        step.name = jsonStep.getString("name")
                        step.material = jsonStep.getString("material")
                        step.length = jsonStep.getInt("length")
                        stepList.add(step)
                        Log.d(javaClass.simpleName, "Number: " + i + ", Neighborhood: " + step.neighborhood + ", Name: " + step.name + ", Material: " + step.material + ", Length: " + step.length)
                    }

                    //TODO: why is recyclerview cutting off last item in larger lists
                    //set up the recyclerview to display list of steps
                    val adapter = RecyclerAdapter(stepList)
                    recyclerView.adapter = adapter

                    }
            }
        }

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        }
    }

