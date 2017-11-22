package com.juliemenge.pittsburghcitysteps

import java.net.URL

class NetworkApi {
    fun getDataFromServer(): String {
        val result = URL("https://data.wprdc.org/api/action/datastore_search?resource_id=43f40ca4-2211-4a12-8b4f-4d052662bb64&limit=5").readText()
        return result
    }
}