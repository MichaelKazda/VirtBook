package com.example.virtbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

class NewUserNoData : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // Firebase auth instance
    private lateinit var googleSignInClient: GoogleSignInClient // Google login instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_no_data)

        findViewById<TextView>(R.id.newUserBookID).text = MyApp.bookID

        // Continue even without data
        findViewById<Button>(R.id.newUserNDContinueBtn)?.setOnClickListener {
            // Redirect to main activity
            val mainAcIntent = Intent(this, MainActivity::class.java)
            startActivity(mainAcIntent)
            finish()
        }

        // Come back after car check
        findViewById<Button>(R.id.newUserNDEndBtn)?.setOnClickListener {
            // Logout
            auth = FirebaseAuth.getInstance()   // Firebase auth instance
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }!!
            auth.signOut()
            googleSignInClient.signOut()

            // Redirects to login screen
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
            this.finish()
        }
    }
}