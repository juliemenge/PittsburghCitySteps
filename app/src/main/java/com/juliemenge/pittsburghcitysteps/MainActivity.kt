package com.juliemenge.pittsburghcitysteps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
//import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import android.R.array
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import java.util.Arrays.asList



class MainActivity : AppCompatActivity() {

    val neighborhoods = arrayOf("Allentown", "Arlington", "Banksville", "Bedford Dwellings", "Beechview" +
            "Beltzhoover", "Bloomfield", "Bluff", "Bon Air", "Brighton Heights", "Brookline" +
            "California-Kirkbride", "Carrick", "Central Lawrenceville", "Central Northside" +
            "Central Oakland", "Chartiers City", "Crafton Heights", "Crawford-Roberts", "Duquesne Heights" +
            "East Allegheny", "East Carnegie", "East Hills", "Elliott", "Esplen", "Fineview", "Garfield" +
            "Glen Hazel", "Greenfield", "Hazelwood", "Highland Park", "Homewood North", "Knoxville" +
            "Larimer", "Lincoln-Lemington-Belmar", "Lincoln Place", "Lower Lawrenceville" +
            "Marhsall-Shadeland", "Middle Hill", "Morningside", "Mount Oliver Borough", "Mount Washington" +
            "Mt. Oliver", "North Oakland", "Oakwood", "Overbrook", "Perry North", "Perry South" +
            "Point Breeze", "Polish Hill", "Ridgemont", "Shadyside", "Sheraden", "South Oakland" +
            "South Side Flats", "South Side Slopes", "Spring Garden", "Spring Hill-City View" +
            "Squirrel Hill North", "Squirrel Hill South", "Stanton Heights", "St. Clair", "Strip District" +
            "Terrace Village", "Troy Hill", "Upper Hill", "Upper Lawrenceville", "West End" +
            "West Oakland", "Westwood", "Windgap")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //spinner to select neighborhood
        val aa: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, neighborhoods)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        neighborhoodSpinner.adapter = aa

        //base list of recycling dates based on chosen neighborhood
        neighborhoodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val rawNeighborhoodChoice = neighborhoodSpinner.selectedItem.toString()
                val neighborhood = "'" + rawNeighborhoodChoice + "'"
            }
        }



        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        val rawNeighborhoodChoice = "Upper Lawrenceville" //TODO: will probably get this from a spinner, adding leading and trailing apostrophes below
        val neighborhood = "'" + rawNeighborhoodChoice + "'"

        async(UI) {
            val network = NetworkApi() //pass in user selected neighborhood
            val data = bg { network.getDataFromServer(neighborhood) }
            val useableData = data.await()

            val allthedata = JSONObject(useableData) //get one giant blob of json data
            val result = allthedata.getJSONObject("result") //create a json blob of just the result data
            val records = result.getJSONArray("records") //get an array of the records

            val arraySize = records.length()

            //val stepList: Array<Step?> = arrayOfNulls<Step>(records.length()) //create an array of steps with no data
            //val stepList: ArrayList<Step?> = ArrayList<Step?>(records.length())
            val stepList = ArrayList<Step>(arraySize)

            //iterate through json data and create array of steps objects

            //for (i in stepList.indices) {
            for (i in 0..arraySize-1) {
                val jsonStep = records.getJSONObject(i) //do stuff with the first step
                val step = Step() //create a new step object
                step.neighborhood = jsonStep.getString("neighborhood")
                step.name = jsonStep.getString("name")
                step.material = jsonStep.getString("material")
                step.length = jsonStep.getInt("length")
                //stepList[i] = step
                stepList.add(step)
                Log.d(javaClass.simpleName, "Number: " + i + ", Neighborhood: " + step.neighborhood + ", Name: " + step.name + ", Material: " + step.material + ", Length: " + step.length)
            }

            //val newStepList = ArrayList(Arrays.asList(stepList))

            //val newStepList = stepList.toCollection(ArrayList())

            val adapter = RecyclerAdapter(stepList)
            recyclerView.adapter = adapter

                //TODO: create a recycler view to display steplist
                Log.d(javaClass.simpleName, "HI THERE Neighborhood: " + stepList[0]!!.neighborhood + ", Name: " + stepList[0]!!.name + ", Material: " + stepList[0]!!.material + ", Length: " + stepList[0]!!.length)
                //textView.setText("Neighborhood: " + step.neighborhood + ", Name: "+ step.name + ", Material: "  + step.material + ", Length: " + step.length)
            }

        }
    }

