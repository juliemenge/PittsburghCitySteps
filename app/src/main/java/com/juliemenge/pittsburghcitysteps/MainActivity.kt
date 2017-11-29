package com.juliemenge.pittsburghcitysteps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import android.R.array
import java.util.Arrays.asList



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        val rawNeighborhoodChoice = "Upper Lawrenceville" //TODO: will probably get this from a spinner, adding leading and trailing apostrophes below
        val neighborhoodChoice = "'" + rawNeighborhoodChoice + "'"

        async(UI) {
            val network = NetworkApi() //pass in user selected neighborhood
            val data = bg { network.getDataFromServer(neighborhoodChoice) }
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

