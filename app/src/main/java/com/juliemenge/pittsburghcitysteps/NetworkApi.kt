package com.juliemenge.pittsburghcitysteps

import android.util.Log
import org.json.JSONObject
import java.net.URL

class NetworkApi {
    fun getDataFromServer(neighborhood: String): List<Step> {
        // Review: Use String Templates as opposed to concatenation
        val storage = "\"43f40ca4-2211-4a12-8b4f-4d052662bb64\""
        val json = URL("https://data.wprdc.org/api/action/datastore_search_sql?sql=SELECT * from $storage WHERE neighborhood LIKE $neighborhood").readText()
        val records = JSONObject(json).getJSONObject("result").getJSONArray("records")
        val stepList = mutableListOf<Step>()
        (0 until records.length()).forEach { i ->
            val jsonStep = records.getJSONObject(i) //do stuff with the first step
            val step = Step(jsonStep.getString("neighborhood"), jsonStep.getString("name")
                    , jsonStep.getString("material"), jsonStep.getInt("length"), jsonStep.getString("image")) //create a new step object

            stepList.add(step)
            Log.d(javaClass.simpleName, "Number: $i, Neighborhood: ${step.neighborhood}, Name: ${step.name}, Material: ${step.material}, Length: ${step.length}")
        }
        return stepList
    }
}