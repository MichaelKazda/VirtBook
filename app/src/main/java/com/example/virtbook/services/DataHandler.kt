package com.example.virtbook.services

import android.util.Log
import com.example.virtbook.MyApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Class for handling data, but not accessing them
 */
class DataHandler {

    private val db = Firebase.firestore

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

    // Saves notificationToken to DB
    fun saveNotifToken(notifToken: String) {
        try {
            db.collection("users").document(MyApp.userID).update("notificationToken", notifToken)
        }catch (e: Exception){
            Log.e("SavingNotifToken Err", notifToken)
        }
    }

}