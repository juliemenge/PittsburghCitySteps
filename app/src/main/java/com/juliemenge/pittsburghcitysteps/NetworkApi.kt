package com.juliemenge.pittsburghcitysteps

import java.net.URL

class NetworkApi {
    fun getDataFromServer(neighborhood: String): String {

        val incompleteResult = URL("https://data.wprdc.org/api/action/datastore_search_sql?sql=SELECT * from \"43f40ca4-2211-4a12-8b4f-4d052662bb64\" WHERE neighborhood LIKE ")
        val result = URL(incompleteResult.toString() + neighborhood).readText()

        //val incompleteURL = URL("https://data.wprdc.org/api/action/datastore_search_sql?sql=SELECT * from \"43f40ca4-2211-4a12-8b4f-4d052662bb64\" WHERE neighborhood LIKE ")
        //val completeUrl = URL(incompleteURL.toString() + neighborhood)

        return result
    }
}