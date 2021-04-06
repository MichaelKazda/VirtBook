package com.example.virtbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.virtbook.services.DataHandler
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal

class MainActivity : AppCompatActivity(), OSSubscriptionObserver {
    private val dataHandler = DataHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OneSignal.addSubscriptionObserver(this)
    }

    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
        if (!stateChanges!!.from.subscribed && stateChanges.to.subscribed) {
            // Getting notificationToken and saving into DB
            val notifToken = stateChanges.to.userId
            dataHandler.saveNotifToken(notifToken)
        }
    }

}