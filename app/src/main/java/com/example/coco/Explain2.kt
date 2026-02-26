package com.example.coco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.coco.fragments.two.Fragment1zad
import com.example.coco.fragments.threefour.zadaci.fragment1prikazrez
import com.example.coco.fragments.threefour.zadaci.fragment2prikazrez
import com.example.coco.fragments.two.Fragment2zad
import com.example.coco.fragments.two.fragment1igrac
import com.example.coco.fragments.two.fragment1kraj
import com.example.coco.fragments.two.fragment2igrac
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID


class Explain2 : AppCompatActivity() {



    val numberOfFragments = 2
    var numberOfChecked = 0
    var numberOfChecked2 = 0
    var playeramount = 0
    val fragment1igrac = fragment1igrac()
    val fragment2igrac = fragment2igrac()
    val fragment1zad = Fragment1zad()
    val fragment2zad = Fragment2zad()
    val fragment1prikazrez = fragment1prikazrez()
    val fragment2prikazrez = fragment2prikazrez()
    val fragment1kraj = fragment1kraj()
    var ETtimer = 0
    var ime1 = ""
    var ime2 = ""
    var state2 = false
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""
    var counter1 = 0
    var counter2 = 0
    var nextfragment2 = false


    private var backHoldRunnable: Runnable? = null
    private val HOLD_THRESHOLD = 3000L // 3 seconds

    private fun initializeSession() {
        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)

        val newSessionId = UUID.randomUUID().toString()
        val tabletId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val student1 = sharedPref.getString("ime1", "unknown") ?: "unknown"
        val student2 = sharedPref.getString("ime2", "unknown") ?: "unknown"


        val sessionData = hashMapOf(
            "tabletId" to tabletId,
            "activityId" to UUID.randomUUID().toString(),
            "timestamp" to FieldValue.serverTimestamp(),
            "students" to listOf(student1, student2)
        )

        val sessionRef = Firebase.firestore.collection("activitySessions").document(newSessionId)
        sessionRef.set(sessionData).addOnSuccessListener {
            Log.d("Firebase", "Session created: $newSessionId")
            sharedPref.edit().putString("sessionId", newSessionId).apply()

            val playersData = listOf(Pair("player1", student1), Pair("player2", student2))
            playersData.forEach { (fixedPlayerId, playerName) ->
                val playerDocId = UUID.randomUUID().toString()
                val playerData = hashMapOf(
                    "imeUcenika" to playerName,
                    "tocni" to 0,
                    "netocni" to 0,
                    "ukupno" to 0,
                    "konfiguracija" to "1:2",
                    "timestamp" to FieldValue.serverTimestamp(),
                    "krajAktivnosti" to null,
                    "vrijeme" to ETtimer
                )
                sessionRef.collection("players").document(playerDocId).set(playerData)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Player doc created for $playerName")
                        sharedPref.edit().putString("${fixedPlayerId}_docId", playerDocId).apply()
                    }
                    .addOnFailureListener {
                        Log.e("Firebase", "Failed to create player doc for $playerName", it)
                    }
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Failed to create session", it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explain2)

        initializeSession()

        val frag1 = findViewById<FrameLayout>(R.id.fragment1)
        val frag2 = findViewById<FrameLayout>(R.id.fragment2)

        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)
        sharedPref.edit().putInt("counter1", 0).apply()
        sharedPref.edit().putInt("counter2", 0).apply()
        playeramount = sharedPref.getInt("amount", 1)
        ime1 = sharedPref.getString("ime1", "Default").toString()
        ime2 = sharedPref.getString("ime2", "Default").toString()

        supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1igrac).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2igrac).commit()

        ETtimer = sharedPref.getInt("timer", 5)

        msg1 = "Unutar sljedeće $ETtimer minute pokušaj \nriješiti što više zadataka."
        msg2 = "Unutar sljedeće minute pokušaj \nriješiti što više zadataka."
        msg3 = "Unutar sljedećih $ETtimer minuta pokušaj \nriješiti što više zadataka."

        frag1.setOnClickListener {
            if (nextfragment2) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1prikazrez).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2prikazrez).commit()
            }
        }

        frag2.setOnClickListener {
            if (nextfragment2) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1prikazrez).commit()
                supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2prikazrez).commit()
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            when (event.action) {
                KeyEvent.ACTION_DOWN -> {
                    // If not already running, start the delayed exit
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

        if (numberOfChecked == numberOfFragments) {

            state2 = true

            if (ETtimer in 2..4) {
                fragment1igrac.update(msg1, ime1)
                fragment2igrac.update(msg1, ime2)

            } else if (ETtimer == 1) {

                fragment1igrac.update(msg2, ime1)
                fragment2igrac.update(msg2, ime2)
            } else {
                fragment1igrac.update(msg3, ime1)
                fragment2igrac.update(msg3, ime2)

            }

        }

    }

    fun removeCheck() {
        numberOfChecked -= 1

    }

    fun s2addcheck() {
        numberOfChecked2 += 1
        if (numberOfChecked2 == 2) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1zad).commit()
            supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2zad).commit()
        }

    }

        fun s2rmvcheck() {
            numberOfChecked2 -= 1

        }

    fun goback(){
        supportFragmentManager.beginTransaction().replace(R.id.fragment1, fragment1prikazrez).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragment2, fragment2prikazrez).commit()

    }

    fun gotoend(){
        val Intent = Intent(this,Kraj::class.java)
        startActivity(Intent)

    }

}

