package com.example.virtbook.services

import android.util.Log
import com.example.virtbook.MyApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder
import kotlin.math.roundToInt

/**
 * Class for handling data
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

    // Counts and returns car maintenance score
    fun getMaintenanceScore(oilReminderDaysDiff: Long, STKReminderDaysDiff: Long, kmCheckReminder: Long, kmCount: Long, fixCount: Long, errorsCount: Long ): Int {
        // Max score
        var score = 100.0;

        // 1st PART: Number of errors in technical check (50 %)
        if(errorsCount >= 50){
            score -= 50
        }else{
            score -= errorsCount;
        }

        // 2nd PART: Fulfilled reminders (30 %)
        if(oilReminderDaysDiff <= 0){
            score -= 10
        }
        if (STKReminderDaysDiff <= 0){
            score -= 10
        }
        if(kmCheckReminder <= 0){
            score -= 10
        }

        // 3rd PART: Number of km per fix count (20 %)
        if(fixCount > 0) {
            val kmPerFixCount = kmCount / fixCount
            if (kmPerFixCount <= 500) {
                score -= 20
            } else if (kmPerFixCount <= 1000) {
                score -= 16
            } else if (kmPerFixCount <= 2000) {
                score -= 12
            } else if (kmPerFixCount <= 3000) {
                score -= 8
            } else if (kmPerFixCount <= 5000) {
                score -= 4
            }
        }

        return score.roundToInt();
    }

    // Saves notificationToken to DB
    fun saveNotifToken(notifToken: String) {
        try {
            db.collection("users").document(MyApp.userID).update("notificationToken", notifToken)
        }catch (e: Exception){
            Log.e("Saving notif token", e.toString())
        }
    }

    // Generate bookID
    fun genBookID(): String{
        val chars = ('A'..'Z') + ('0'..'9')  // Allowed characters - only caps and numbers
        val bookID = StringBuilder()
        for (i in 1..5){
            bookID.append(chars.random())
        }
        return bookID.toString()
    }

    // Insert new user into DB
    fun newUserInDB(bookID: String, googleLoginToken: String, email: String?, name: String?, carBrand: String, carModel: String): String {
        // Set up data in format
        val user = hashMapOf(
            "active" to true,
            "bookID" to bookID,
            "carData" to hashMapOf(
                "brand" to carBrand,
                "carCheck" to "",
                "model" to carModel,
                "reminders" to hashMapOf(
                    "dateOilChange" to 0,
                    "dateSTK" to 0,
                    "kmNextCheck" to 0
                ),
                "repairHistory" to arrayListOf<HashMap<*,*>>(),
                "stats" to hashMapOf(
                    "costsTotal" to 0,
                    "errorsTotal" to 0,
                    "fixPrice" to 0,
                    "kmTotal" to 0,
                    "registerDay" to System.currentTimeMillis(),
                    "repairsTotal" to 0
                )
            ),
            "e-mail" to email,
            "googleLoginToken" to googleLoginToken,
            "name" to name,
            "notificationToken" to "",
        )

        try {
            // Saving into DB
            val newDoc = db.collection("users").document()
            newDoc.set(user)
            // Return id of newly created document
            return newDoc.id
        }catch (e: Exception){
            Log.e("saving new user", e.toString())
            return ""
        }
    }
}