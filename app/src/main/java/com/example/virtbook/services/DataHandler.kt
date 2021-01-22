package com.example.virtbook.services

import com.google.firebase.firestore.DocumentSnapshot


/**
 * Class for handling data, but not accessing them
 */
class DataHandler {

    // Returns data maps for garage site
    fun getDataGarage(docRef: DocumentSnapshot, data: String ): Map<*,*> {
        val userData = docRef.data as Map<String, Any>
        val carData = userData["carData"] as Map<*, *>

        return when (data){
            "userData" -> userData
            "carData" -> carData
            "stats" -> carData["stats"] as Map<*, *>
            "reminders" -> carData["reminders"] as Map<*, *>
            else -> {
                userData
            }
        }
    }

}