package com.example.virtbook

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService : FirebaseMessagingService(){

    // Called when new token is generated
    override fun onNewToken(token: String) {
        Log.d("TAG", "New token: $token")

        // TODO Sends new token to server and registers it
        //registerInstanceOnServer(token)
    }

}