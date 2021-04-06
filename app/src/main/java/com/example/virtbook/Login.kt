package com.example.virtbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val db = Firebase.firestore // Access Firestore DB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Firebase auth instance
        auth = FirebaseAuth.getInstance()

        // Google sign in button listener
        findViewById<Button>(R.id.googleSingInBtn)?.setOnClickListener{
            signIn()
        }
    }

    // Open Google sign in window
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 124)
    }

    // Handling Google sign in response
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent
        if (requestCode == 124) {
            val signInTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(signInTask.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = signInTask.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed
                    findViewById<TextView>(R.id.signInError)?.text = getString(R.string.loginFailureMsg)
                }
            }
        }
    }

    // Firebase authentication
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Authentication was successful
                    db.collection("users")
                        .whereEqualTo("googleLoginToken", auth.currentUser.uid)
                        .get()
                        .addOnSuccessListener { result ->
                            if (result != null && result.documents.isNotEmpty()) {
                                // User is already in database
                                MyApp.userID = result.documents[0].id
                                val mainAcIntent = Intent(this, MainActivity::class.java)
                                startActivity(mainAcIntent)
                                finish()
                            } else {
                                // User is not in database -> first time user
                                val newUserIntent = Intent(this, NewUser::class.java)
                                startActivity(newUserIntent)
                                finish()
                            }
                        }
                        .addOnFailureListener{
                            findViewById<TextView>(R.id.signInError)?.text = getString(R.string.loginFailureMsg)
                        }
                } else {
                    // Authentication failed
                    findViewById<TextView>(R.id.signInError)?.text = getString(R.string.loginFailureMsg)
                }
            }
    }
}