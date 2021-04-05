package com.example.virtbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.virtbook.services.DataHandler
import com.onesignal.OneSignal

class MainActivity : AppCompatActivity() {

    private val dataHandler = DataHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*SETTING UP ONESIGNAL NOTIFICATION*/
        // Notification logs
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        // Getting notificationToken and saving into DB
        val notifToken = OneSignal.getPermissionSubscriptionState().subscriptionStatus.userId
        dataHandler.saveNotifToken(notifToken)
    }

}