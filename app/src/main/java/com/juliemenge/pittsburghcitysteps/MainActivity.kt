package com.juliemenge.pittsburghcitysteps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    internal lateinit var list: ArrayList<Step>
    internal lateinit var listView: ListView
    internal lateinit var stepList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        async(UI){
            val network = NetworkApi()
            val data = bg{network.getDataFromServer()}
            val useableData = data.await()

            val allthedata = JSONObject(useableData) //get one giant blob of json data
            val result = allthedata.getJSONObject("result") //create a json blob of just the result data
            val records = result.getJSONArray("records") //get an array of the records
            Log.d(javaClass.simpleName, "HERE'S THE DATA" + records)

            val steps = arrayOfNulls<Step>(records.length()) //create an array of steps with no data
            val jsonStep = records.getJSONObject(1) //do stuff with the first step
            val step = Step() //create a new step object
            step.neighborhood = jsonStep.getString("neighborhood")
            step.name = jsonStep.getString("name")
            step.material = jsonStep.getString("material")
            steps[1] = step

            Log.d(javaClass.simpleName, "Neighborhood: " + step.neighborhood + ", Name: " + step.name + ", Material: " + step.material)
            textView.setText("Neighborhood: " + step.neighborhood + ", Name: "+ step.name + ", Material: "  + step.material)
        }
    }
}
