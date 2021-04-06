package com.example.virtbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.virtbook.services.DataHandler
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal

class NewUser : AppCompatActivity() {
    private var dataHandler = DataHandler()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        // Check car info inputs
        findViewById<Button>(R.id.newUserBtn)?.setOnClickListener {
            // Get values from car info inputs and strip special chars
            val regex = Regex("[^A-Za-z0-9á-žÁ-Ž ]")
            val carBrand = regex.replace(findViewById<EditText>(R.id.carBrandInput).text, "")
            val carModel = regex.replace(findViewById<EditText>(R.id.carModelInput).text, "")

            if(carModel.isNotEmpty() && carBrand.isNotEmpty()){
                // Generate new bookID
                val bookID = dataHandler.genBookID()
                MyApp.bookID = bookID

                // Save new user into DB and get userID
                auth = FirebaseAuth.getInstance()
                MyApp.userID = dataHandler.newUserInDB(bookID, auth.currentUser.uid, auth.currentUser.email, auth.currentUser.displayName, carBrand, carModel)

                // Setting up OneSignal notification, sending request for notification token to OneSignal server
                OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
                OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init()

                // Redirect to new user message activity
                val noDataMsgIntent = Intent(this, NewUserNoData::class.java)
                startActivity(noDataMsgIntent)
                finish()
            }else{
                findViewById<TextView>(R.id.newUserError).text = getString(R.string.newUserError)
            }
        }
    }
}