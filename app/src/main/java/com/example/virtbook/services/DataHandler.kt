package com.example.virtbook.services

import android.util.ArrayMap
import android.util.Log
import com.example.virtbook.MyApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt


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
            Log.e("SavingNotifToken Err", notifToken)
        }
    }

}