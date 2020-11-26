
package com.example.virtbook

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            var checksData = ""
            db.collection("checks").get().addOnSuccessListener { result ->
                for (document in result){
                    val price = document.data["price"]
                    val kmCount = document.data["kmCount"]
                    val checkArea = document.data["checkArea"]
                    checksData += "Cena -> $price Kč, Aktuálně najeto -> $kmCount km, Opraveno -> $checkArea \n"
                    view.findViewById<TextView>(R.id.textview_first).text = checksData
                }
            }
        }
    }
}