package com.example.virtbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class Loader : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        val logChecker = FirebaseAuth.getInstance().currentUser

        // Waits 1,5sec and checks if user is logged in. If not redirect to login
        Handler(Looper.getMainLooper()).postDelayed({
            if(logChecker != null){
                val mainAcIntent = Intent(this, MainActivity::class.java)
                startActivity(mainAcIntent)
                finish()
            }else{
                val loginIntent = Intent(this, Login::class.java)
                startActivity(loginIntent)
                finish()
            }
        }, 2500)
    }
}