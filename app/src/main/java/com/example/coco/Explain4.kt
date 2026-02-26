package com.example.coco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.coco.fragments.threefour.Fragment1zadManji
import com.example.coco.fragments.threefour.Fragment2zadManji
import com.example.coco.fragments.threefour.Fragment3zadManji
import com.example.coco.fragments.threefour.Fragment4zadManji
import com.example.coco.fragments.threefour.Igraci.Fragment1igracManji
import com.example.coco.fragments.threefour.Igraci.Fragment2igracManji
import com.example.coco.fragments.threefour.Igraci.Fragment3igracManji
import com.example.coco.fragments.threefour.Igraci.Fragment4igracManji
import com.example.coco.fragments.threefour.rezultati.fragment1prikazrezmanji
import com.example.coco.fragments.threefour.rezultati.fragment2prikazrezmanji
import com.example.coco.fragments.threefour.rezultati.fragment3prikazrezmanji
import com.example.coco.fragments.threefour.rezultati.fragment4prikazrezmanji
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID


class Explain4 : AppCompatActivity() {
    var numberOfFragments = 4
    var numberOfChecked = 0
    var numberOfChecked2 = 0
    var playeramount = 0
    val fragment1igracManji = Fragment1igracManji()
    val fragment2igracManji = Fragment2igracManji()
    val fragment3igracManji = Fragment3igracManji()
    val fragment4igracManji = Fragment4igracManji()
    val fragment1zadManji = Fragment1zadManji()
    val fragment2zadManji = Fragment2zadManji()
    val fragment3zadManji = Fragment3zadManji()
    val fragment4zadManji = Fragment4zadManji()
    val fragment1prikazrez = fragment1prikazrezmanji()
    val fragment2prikazrez = fragment2prikazrezmanji()
    val fragment3prikazrez = fragment3prikazrezmanji()
    val fragment4prikazrez = fragment4prikazrezmanji()
    var ETtimer = 0
    var ime1 = ""
    var ime2 = ""
    var ime3 = ""
    var ime4 = ""
    var state2 = false
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""
    var counter1manji = 0
    var counter2manji = 0
    var counter3manji = 0
    var counter4manji = 0
    var nextfragment2 = false

    private var backHoldRunnable: Runnable? = null
    private val HOLD_THRESHOLD = 3000L // 3 seconds

    private fun initializeSession() {
        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)
        val existingSessionId = sharedPref.getString("sessionId", null)
        playeramount = sharedPref.getInt("amount", 1)

        if (existingSessionId == null) {
            val tabletId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val newSessionId = UUID.randomUUID().toString()


            val allStudentNames = listOf(
                sharedPref.getString("ime1", "unknown") ?: "unknown",
                sharedPref.getString("ime2", "unknown") ?: "unknown",
                sharedPref.getString("ime3", "unknown") ?: "unknown",
                sharedPref.getString("ime4", "unknown") ?: "unknown"
            )

            val allPlayersData = listOf(
                Pair("player1", allStudentNames[0]),
                Pair("player2", allStudentNames[1]),
                Pair("player3", allStudentNames[2]),
                Pair("player4", allStudentNames[3])
            )

            val activePlayersData = allPlayersData.take(playeramount)
            val activeStudentNames = activePlayersData.map { it.second } // List of names that will be used

            val sessionData = hashMapOf(
                "tabletId" to tabletId,
                "activityId" to UUID.randomUUID().toString(),
                "timestamp" to FieldValue.serverTimestamp(),
                "students" to activeStudentNames
            )


            //  Write session doc
            val sessionRef = Firebase.firestore.collection("activitySessions").document(newSessionId)

            //  Write session doc
            sessionRef.set(sessionData)
                .addOnSuccessListener {
                    Log.d("Firebase", "Session created: $newSessionId")
                    sharedPref.edit().putString("sessionId", newSessionId).apply()


                    activePlayersData.forEach { (fixedPlayerId, playerName) ->
                        val playerDocId = UUID.randomUUID().toString()

                        val playerData = hashMapOf(
                            "tabletId" to tabletId,
                            "imeUcenika" to playerName,
                            "tocni" to 0,
                            "netocni" to 0,
                            "ukupno" to 0,
                            "konfiguracija" to "1:$playeramount",
                            "timestamp" to FieldValue.serverTimestamp(),
                            "krajAktivnosti" to null,
                            "vrijeme" to ETtimer
                        )

                        sessionRef.collection("players").document(playerDocId).set(playerData)
                            .addOnSuccessListener {
                                Log.d("Firebase", "Player doc created for $playerName ($fixedPlayerId) with id $playerDocId")

                                sharedPref.edit().putString("${fixedPlayerId}_docId", playerDocId).apply()
                            }
                            .addOnFailureListener {
                                Log.e("Firebase", "Failed to create player doc for $playerName", it)
                            }
                    }
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Failed to create session", it)
                }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explain4)

        initializeSession()

        Toast.makeText(this, "Player amount: $playeramount", Toast.LENGTH_SHORT).show()

        val frag1 = findViewById<FrameLayout>(R.id.fragment1)
        val frag2 = findViewById<FrameLayout>(R.id.fragment2)
        val frag3 = findViewById<FrameLayout>(R.id.fragment3)
        val frag4 = findViewById<FrameLayout>(R.id.fragment4)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.overlayex) // Get the root ConstraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout) // Clone current constraints

        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)
        playeramount = sharedPref.getInt("amount",1)
        numberOfFragments = playeramount
        ime1 = sharedPref.getString("ime1","Default").toString()
        ime2 = sharedPref.getString("ime2","Default").toString()
        ime3 = sharedPref.getString("ime3","Default").toString()
        ime4 = sharedPref.getString("ime4","Default").toString()

        when (playeramount){
            3 -> {
                // Set the correct fragments for 3 players
                supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1igracManji).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2igracManji).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment3, fragment3igracManji).commit()

                // Hide fragment4 to create space for centering fragment3
                frag4.visibility = View.GONE

                // Apply constraints to center fragment3
                constraintSet.clone(constraintLayout)


                constraintSet.clear(R.id.fragment3, ConstraintSet.START)
                constraintSet.clear(R.id.fragment3, ConstraintSet.END)
                constraintSet.clear(R.id.fragment3, ConstraintSet.TOP)
                constraintSet.clear(R.id.fragment3, ConstraintSet.BOTTOM)


                constraintSet.connect(R.id.fragment3, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(R.id.fragment3, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.connect(R.id.fragment3, ConstraintSet.TOP, R.id.guideline_h_50, ConstraintSet.TOP)
                constraintSet.connect(R.id.fragment3, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)


                constraintSet.constrainPercentWidth(R.id.fragment3, 0.5f)


                constraintSet.applyTo(constraintLayout)
            }
            4 -> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1igracManji).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2igracManji).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment3, fragment3igracManji).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment4, fragment4igracManji).commit()


                frag4.visibility = View.VISIBLE


                constraintSet.clone(this, R.layout.activity_explain4)
                constraintSet.applyTo(constraintLayout)
            }
        }

        ETtimer = sharedPref.getInt("timer", 5)
        msg1 = "Unutar sljedeće $ETtimer minute pokušaj \nriješiti što više zadataka."
        msg2 = "Unutar sljedeće minute pokušaj \nriješiti što više zadataka."
        msg3 = "Unutar sljedećih $ETtimer minuta pokušaj \nriješiti što više zadataka."


        listOf(frag1, frag2, frag3, frag4).forEach { frag ->
            frag.setOnClickListener {
                if (nextfragment2) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1prikazrez).commit()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2prikazrez).commit()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment3, fragment3prikazrez).commit()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment4, fragment4prikazrez).commit()
                }
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            when (event.action) {
                KeyEvent.ACTION_DOWN -> {

                    if (backHoldRunnable == null) {
                        backHoldRunnable = Runnable {
                            super.onBackPressed() // exit after 3s hold
                        }
                        window.decorView.postDelayed(backHoldRunnable!!, HOLD_THRESHOLD)
                    }
                    return true // ignore short press
                }
                KeyEvent.ACTION_UP -> {
                    // Cancel if released early
                    backHoldRunnable?.let { window.decorView.removeCallbacks(it) }
                    backHoldRunnable = null
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }


    fun addCheck() {
        numberOfChecked += 1

        if ((numberOfChecked == 4 && playeramount == 4) || (numberOfChecked == 3 && playeramount == 3)) {
            state2 = true
            when (ETtimer) {
                in 2..4 -> updateAllFragments(msg1)
                1 -> updateAllFragments(msg2)
                else -> updateAllFragments(msg3)
            }
        }
    }

    private fun updateAllFragments(msg: String) {
        if (playeramount >= 1) fragment1igracManji.update(msg, ime1)
        if (playeramount >= 2) fragment2igracManji.update(msg, ime2)
        if (playeramount >= 3) fragment3igracManji.update(msg, ime3)
        if (playeramount >= 4) fragment4igracManji.update(msg, ime4)
    }

    fun removeCheck() {
        numberOfChecked -= 1
    }

    fun s2addcheck() {
        numberOfChecked2 += 1

        if ((numberOfChecked2 == 4 && playeramount == 4) || (numberOfChecked2 == 3 && playeramount == 3)) {
            if (playeramount >= 1) supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1zadManji).commit()
            if (playeramount >= 2) supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2zadManji).commit()
            if (playeramount >= 3) supportFragmentManager.beginTransaction().replace(R.id.fragment3, fragment3zadManji).commit()
            if (playeramount >= 4) supportFragmentManager.beginTransaction().replace(R.id.fragment4, fragment4zadManji).commit()
        }
    }

    fun s2rmvcheck() {
        numberOfChecked2 -= 1
    }

    fun gotoend() {
        val intent = Intent(this,Kraj::class.java)
        startActivity(intent)
    }
}


