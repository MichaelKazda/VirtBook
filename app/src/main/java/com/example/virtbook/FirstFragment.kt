
package com.example.virtbook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anychart.AnyChartView
import com.example.virtbook.services.DataHandler
import com.example.virtbook.services.GraphicsMaker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main garage site
 */
class FirstFragment : Fragment() {
    private val graphicsMaker = GraphicsMaker() // Handling graphics
    private val dataHandler = DataHandler() // Handling data
    private val db = Firebase.firestore // Access Firestore DB
    private lateinit var auth: FirebaseAuth // Firebase auth instance
    private lateinit var googleSignInClient: GoogleSignInClient // Google login instance

    // Gets data from DB and binds it to frontend
    @SuppressLint("SetTextI18n")
    fun bindData(){
        /*GETTING DATA*/
        db.collection("users").document(MyApp.userID).get().addOnSuccessListener { result ->
            if (result != null){
                // Obtaining data
                val carData = dataHandler.getDataGarage(result, "carData")
                val userData = dataHandler.getDataGarage(result, "userData")
                val reminders = dataHandler.getDataGarage(result, "reminders")
                val stats = dataHandler.getDataGarage(result, "stats")
                val repairHistory = carData["repairHistory"] as ArrayList<*>

                /*BINDING DATA TO FRONTEND*/
                // Car section
                view?.findViewById<TextView>(R.id.carBrand)?.text = carData["brand"].toString() // Car brand
                view?.findViewById<TextView>(R.id.carModel)?.text = carData["model"].toString() // Car model
                view?.findViewById<TextView>(R.id.bookID)?.text = userData["bookID"].toString() // Book ID

                // Reminders section
                // Car check KM reminder
                view?.findViewById<TextView>(R.id.kmCheckCount)?.text = graphicsMaker.spanIndex(reminders["kmNextCheck"] as Long,"km", resources.getDimensionPixelSize(R.dimen.big_index))
                view?.findViewById<TextView>(R.id.kmCheckCount)?.setTextColor(Color.parseColor(graphicsMaker.kmCountColor(reminders["kmNextCheck"] as Long)))
                // Oil change reminder
                view?.findViewById<TextView>(R.id.oilCheckDate)?.text = SimpleDateFormat("dd. MM. yyyy",  Locale.getDefault()).format(Timestamp(reminders["dateOilChange"] as Long))
                view?.findViewById<TextView>(R.id.oilCheckDate)?.setTextColor(Color.parseColor(graphicsMaker.dateColor(Timestamp(reminders["dateOilChange"] as Long))))
                // STK Check reminder
                view?.findViewById<TextView>(R.id.fixCount)?.text = SimpleDateFormat("dd. MM. yyyy",  Locale.getDefault()).format(Timestamp(reminders["dateSTK"] as Long))
                view?.findViewById<TextView>(R.id.fixCount)?.setTextColor(Color.parseColor(graphicsMaker.dateColor(Timestamp(reminders["dateSTK"] as Long))))

                // Car maintenance graph section
                val score = dataHandler.getMaintenanceScore(
                        graphicsMaker.tsToDaysConv(Timestamp(reminders["dateOilChange"] as Long)),
                        graphicsMaker.tsToDaysConv(Timestamp(reminders["dateSTK"] as Long)),
                        reminders["kmNextCheck"] as Long,
                        stats["kmTotal"] as Long,
                        stats["repairsTotal"] as Long,
                        stats["errorsTotal"] as Long
                )
                view?.findViewById<TextView>(R.id.scoreNumber)?.text = score.toString() + "%"
                // Creating graph with score
                val anyChartView = view?.findViewById<View>(R.id.scoreGraph) as AnyChartView
                anyChartView.setChart(graphicsMaker.createGraph(score))

                // Stats section
                val indexSizeSmall = resources.getDimensionPixelSize(R.dimen.small_index)
                view?.findViewById<TextView>(R.id.statsTotalKmNumber)?.text = graphicsMaker.spanIndex(stats["kmTotal"] as Long,"km", indexSizeSmall) // Total km
                view?.findViewById<TextView>(R.id.statsTotalCostsNumber)?.text = graphicsMaker.spanIndex(stats["costsTotal"] as Long,"Kč", indexSizeSmall) // Total costs
                view?.findViewById<TextView>(R.id.statsTotalFixesNumber)?.text = graphicsMaker.spanIndex(stats["repairsTotal"] as Long,"úkonů", indexSizeSmall) // Total fixes
                view?.findViewById<TextView>(R.id.statsTotalDaysNumber)?.text = graphicsMaker.spanIndex(graphicsMaker.tsToDaysConv(Timestamp(stats["registerDay"] as Long), true),"dní", indexSizeSmall) // Total days


                // Car check button
                view?.findViewById<Button>(R.id.checkBtn)?.setOnClickListener {
                    if(carData["carCheck"].toString().isEmpty()){
                        Toast.makeText(activity, "Nejdříve si nechte vůz prohlídnout.", Toast.LENGTH_SHORT).show()
                    }else{
                        // Creating new activity
                        val intentCarCheck = Intent(activity, SecondActivity::class.java).apply {
                        // Passing data to new activity
                            putExtra("carCheckData", carData["carCheck"].toString())
                            putExtra("errorsTotal", stats["errorsTotal"].toString())
                            putExtra("fixPrice", stats["fixPrice"].toString())
                        }
                        startActivity(intentCarCheck)
                    }
                }

                // Fix history button
                view?.findViewById<Button>(R.id.historyBtn)?.setOnClickListener {
                    if(repairHistory.size <= 0){
                        Toast.makeText(activity, "Nejdříve si nechte vůz opravit.", Toast.LENGTH_SHORT).show()
                    }else {
                        // Creating new activity
                        val intentFixHistory = Intent(activity, FixHistory::class.java).apply {
                            // Passing data to new activity
                            putExtra("fixHistory", repairHistory)
                            putExtra("fixTotal", stats["repairsTotal"].toString())
                            putExtra("costsTotal", stats["costsTotal"].toString())
                        }
                        startActivity(intentFixHistory)
                    }
                }

                // Logout button
                auth = FirebaseAuth.getInstance()   // Firebase auth instance
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!
                view?.findViewById<ImageView>(R.id.logoutIcon)?.setOnClickListener{
                    auth.signOut()
                    googleSignInClient.signOut()
                    val loginIntent = Intent(activity, Login::class.java)
                    startActivity(loginIntent)
                    activity?.finish()
                }

                // Reset refresher
                view?.findViewById<SwipeRefreshLayout>(R.id.refresher)?.isRefreshing = false
            }else{
                // Error, user doesnt exist.
                view?.findViewById<TextView>(R.id.carBrand)?.text = "Uživatel"
                view?.findViewById<TextView>(R.id.carModel)?.text = "Nebyl nalezen"
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()

        /*Refresh data*/
        view.findViewById<SwipeRefreshLayout>(R.id.refresher).setOnRefreshListener {
            bindData()
        }
    }
}
