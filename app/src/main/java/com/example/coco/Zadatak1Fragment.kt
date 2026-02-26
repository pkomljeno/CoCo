package com.example.coco

import android.content.Context
import android.provider.Settings
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.UUID

class Zadatak1Fragment : Fragment() {

    data class MathProblem(val zad: String?, val problem: String?, val solution: String?, val odgovor: String?)
    val mathProblems = arrayListOf<MathProblem>()

    private var najmanji1 = 0
    private var najmanji2 = 0
    private var najveci1 = 0
    private var najveci2 = 0
    private var minrange = 0
    private var maxrange = 0
    private var round1 = false
    private var round2 = false
    private var const1 = 0
    private var const2 = 0
    private var firstbig = false
    private var plusbool = true
    private var minusbool = false
    private var putabool = false
    private var dijeljbool = false

    var tocancnt = 0
    var krivicnt = 0
    var brojaczad = 1
    var odgovor = ""
    private lateinit var cdtimer: CountDownTimer

    companion object {
        var rez1 = 0
        var prvizad1 = ""
        var total = ""
        var result = ""
        var a = 0
        var b = 0
        var c = 0
        var d = 0
        var e = 0
        var f = 0
        var g = 0
        var h = 0
        var i = 0
        var j = 0
        var symbolsArray = mutableListOf<String>()
    }

    fun getTabletId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown_device"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_zadatak1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val username = sharedpref.getString("ime1", "unknown") ?: "unknown"
        val tabletId = getTabletId(requireContext())
        val firestore = Firebase.firestore

        //  Ensure session is created with manual ID
        var sessionId = sharedpref.getString("sessionId", null)
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString()
            sharedpref.edit().putString("sessionId", sessionId).apply()

            // pull number of players from settings
            val savedAmount = sharedpref.getInt("amount", 1)
            val konfiguracija = "1:$savedAmount"

            val sessionData = hashMapOf(
                "sessionId" to sessionId,
                "tabletId" to tabletId,
                "studentName" to username,
                "Konfiguracija" to konfiguracija,
                "timestamp" to FieldValue.serverTimestamp(),
                "endTime" to null,
                "lengthOfActivity" to sharedpref.getInt("timer", 5), // minutes from settings
                "correctAnswers" to 0,
                "wrongAnswers" to 0,
                "totalTasks" to 0
            )

            firestore.collection("activitySessions")
                .document(sessionId)
                .set(sessionData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Session created with ID: $sessionId")
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Neuspjelo spremanje sesije", Toast.LENGTH_SHORT).show()
                }
        }

        // Load math task settings
        najmanji1 = sharedpref.getInt("najm1", 5)
        najmanji2 = sharedpref.getInt("najm2", 5)
        najveci1 = sharedpref.getInt("najv1", 5)
        najveci2 = sharedpref.getInt("najv2", 5)
        round1 = sharedpref.getBoolean("round1", false)
        round2 = sharedpref.getBoolean("round2", false)
        const1 = sharedpref.getInt("const1", 0)
        const2 = sharedpref.getInt("const2", 0)
        firstbig = sharedpref.getBoolean("firstbig", false)
        plusbool = sharedpref.getBoolean("plusbool", true)
        minusbool = sharedpref.getBoolean("minusbool", false)
        putabool = sharedpref.getBoolean("putabool", false)
        dijeljbool = sharedpref.getBoolean("dijeljbool", false)
        minrange = sharedpref.getInt("minrange", 0)
        maxrange = sharedpref.getInt("maxrange", 100)

        result = ""

        spawner()
        generator()

        val prvi = view.findViewById<TextView>(R.id.textView20)
        val unos = view.findViewById<EditText>(R.id.unos)
        val textViewIme = view.findViewById<TextView>(R.id.imeKut)
        val timerView = view.findViewById<TextView>(R.id.vrijeme)
        textViewIme.text = username
        unos.isFocusable = false

        val RealETtimer = sharedpref.getInt("timer", 5).toLong() * 60_000
        Log.d("TimerValue", "Timer from prefs: ${sharedpref.getInt("timer", -1)}")

        cdtimer = object : CountDownTimer(RealETtimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Join all answers into a single string
                val text = mathProblems.joinToString("") {
                    "${it.zad}${it.problem}${it.solution}${it.odgovor}"
                }

                val totalTasks = tocancnt + krivicnt
                val savedAmount = sharedpref.getInt("amount", 1)
                val konfiguracija = "1:$savedAmount"
                val savedTimer = sharedpref.getInt("timer", 5)
                val sessionIdNow = sharedpref.getString("sessionId", null)

                // Save locally to SharedPreferences
                sharedpref.edit()
                    .putInt("tocan", tocancnt)
                    .putInt("ukupan", totalTasks)
                    .putString("zadaci", text)
                    .apply()


                if (sessionIdNow != null) {
                    val firestore = com.google.firebase.ktx.Firebase.firestore

                    val sessionUpdates = hashMapOf(
                        "endTimestamp" to FieldValue.serverTimestamp(),
                        "correctAnswers" to tocancnt,
                        "wrongAnswers" to krivicnt,
                        "totalTasks" to totalTasks,
                        "Konfiguracija" to konfiguracija,
                        "vrijeme" to savedTimer
                    )

                    firestore.collection("activitySessions")
                        .document(sessionIdNow)
                        .update(sessionUpdates)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Session summary saved for $sessionIdNow")
                        }
                        .addOnFailureListener {
                            Log.e("Firestore", "Failed to save session summary: ${it.message}")
                        }
                }

                startActivity(Intent(requireContext(), IstekloVrijeme::class.java))
            }
        }
        cdtimer.start()


        val digits = listOf(
            view.findViewById<ImageFilterView>(R.id.num9) to "1",
            view.findViewById<ImageFilterView>(R.id.num10) to "2",
            view.findViewById<ImageFilterView>(R.id.num11) to "3",
            view.findViewById<ImageFilterView>(R.id.num2) to "4",
            view.findViewById<ImageFilterView>(R.id.num1) to "5",
            view.findViewById<ImageFilterView>(R.id.num) to "6",
            view.findViewById<ImageFilterView>(R.id.num4) to "7",
            view.findViewById<ImageFilterView>(R.id.num3) to "8",
            view.findViewById<ImageFilterView>(R.id.num5) to "9",
            view.findViewById<ImageFilterView>(R.id.num7) to "0"
        )

        digits.forEach { (viewId, value) ->
            viewId.setOnClickListener {
                result += value
                total = result
                unos.setText(result)
            }
        }

        view.findViewById<ImageFilterView>(R.id.num6).setOnClickListener {
            result = result.dropLast(1)
            total = result
            unos.setText(result)
        }

        view.findViewById<ImageFilterView>(R.id.num8).setOnClickListener {
            if (result.isEmpty()) {
                Toast.makeText(requireContext(), "Unesite broj", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userAnswer = result.toInt()
            val correctAnswer = rez1
            val savedAmount = sharedpref.getInt("amount", 1)
            val konfiguracija = "1:$savedAmount"
            val savedTimer = sharedpref.getInt("timer", 5)
            val sessionIdNow = sharedpref.getString("sessionId", null)

            //  Answer data
            val answerData = hashMapOf(
                "username" to username,
                "tabletId" to tabletId,
                "sessionId" to sessionIdNow,
                "Konfiguracija" to konfiguracija,
                "Zadatak" to prvizad1,
                "Odgovor" to userAnswer,
                "TočanOdgovor" to correctAnswer,
                "Točno" to (userAnswer == correctAnswer),
                "timestamp" to FieldValue.serverTimestamp(),
                "vrijeme" to savedTimer
            )

            val playerDocId = sharedpref.getString("${username}_docId", null)
            if (sessionIdNow != null && playerDocId != null) {
                val sessionRef = firestore.collection("activitySessions").document(sessionIdNow)


                sessionRef.collection("answers").add(answerData)


                val playerRef = sessionRef.collection("players").document(playerDocId)
                val updates = hashMapOf<String, Any>(
                    "tabletId" to tabletId,
                    "username" to username,
                    "ukupno" to FieldValue.increment(1),
                    "tocni" to if (userAnswer == correctAnswer) FieldValue.increment(1) else FieldValue.increment(0),
                    "netocni" to if (userAnswer != correctAnswer) FieldValue.increment(1) else FieldValue.increment(0),
                    "krajAktivnosti" to FieldValue.serverTimestamp()
                )
                playerRef.set(updates, com.google.firebase.firestore.SetOptions.merge())
            }


            if (userAnswer == correctAnswer) tocancnt++ else krivicnt++
            brojaczad++
            saveinput()
            result = ""
            total = ""
            unos.setText("")
            spawner()
            generator()
            prvi.text = prvizad1
        }



        prvi.text = prvizad1

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
            val sessionIdNow = sharedpref.getString("sessionId", null)

            if (sessionIdNow != null) {
                val firestore = com.google.firebase.ktx.Firebase.firestore
                val totalTasks = tocancnt + krivicnt
                val savedAmount = sharedpref.getInt("amount", 1)
                val konfiguracija = "1:$savedAmount"
                val savedTimer = sharedpref.getInt("timer", 5)

                val sessionUpdates = hashMapOf(
                    "endTimestamp" to FieldValue.serverTimestamp(),
                    "correctAnswers" to tocancnt,
                    "wrongAnswers" to krivicnt,
                    "totalTasks" to totalTasks,
                    "Konfiguracija" to konfiguracija,
                    "vrijeme" to savedTimer
                )

                firestore.collection("activitySessions")
                    .document(sessionIdNow)
                    .update(sessionUpdates)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Session summary saved on BACK for $sessionIdNow")
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Failed to save session summary on BACK: ${it.message}")
                    }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                // Do nothing when back is pressed
                Toast.makeText(requireContext(), "Povratak je onemogućen tijekom zadatka.", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun spawner() {
        val symbolsArray = mutableListOf<String>()
        if (plusbool) symbolsArray.add("+")
        if (minusbool) symbolsArray.add("-")
        if (putabool) symbolsArray.add("*")
        if (dijeljbool) symbolsArray.add("/")

        if (symbolsArray.isEmpty()) symbolsArray.add("+")

        // Load prefs
        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val useRange = sharedpref.getBoolean("rasponbool", false)
        val minRange = sharedpref.getInt("minrange", 0)
        val maxRange = sharedpref.getInt("maxrange", 100)

        val rnd = symbolsArray.random()

        if (useRange) {

            val minA = najmanji1
            val maxA = najveci1
            val minB = najmanji2
            val maxB = najveci2


            when (rnd) {

                "+" -> {
                    rez1 = (minRange..maxRange).random()

                    a = if (const1 == 1) najmanji1 else (minA..maxA).random()
                    b = rez1 - a

                    if (b < minB || b > maxB) {
                        a = if (const1 == 1) najmanji1 else (minA..maxA).random()
                        b = if (const2 == 1) najmanji2 else (minB..maxB).random()
                        rez1 = a + b
                    }
                }

                "-" -> {
                    var valid = false

                    while (!valid) {
                        a = if (const1 == 1) najmanji1 else (minA..maxA).random()

                        val bMin = maxOf(minB, a - maxrange)
                        val bMax = minOf(maxB, a - minrange)

                        if (bMin <= bMax) {
                            b = if (const2 == 1) najmanji2 else (bMin..bMax).random()
                            rez1 = a - b
                            valid = true
                        }
                    }
                }

                "*" -> {
                    rez1 = (minRange..maxRange).random()

                    val divisors = (1..rez1).filter {
                        rez1 % it == 0 &&
                                it in minA..maxA &&
                                (rez1 / it) in minB..maxB
                    }

                    if (divisors.isNotEmpty()) {
                        a = divisors.random()
                        b = rez1 / a
                    } else {
                        a = if (const1 == 1) najmanji1 else (minA..maxA).random()
                        b = if (const2 == 1) najmanji2 else (minB..maxB).random()
                        rez1 = a * b
                    }
                }

                "/" -> {
                    b = (minB..maxB).filter { it != 0 }.random()
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random()

                    if (a % b != 0) {
                        a = b * ((minRange..maxRange).random())
                    }

                    rez1 = a / b
                }
            }
        }
        else {

            when {
                const1 == 1 -> {
                    a = najmanji1
                    b = (najmanji2..najveci2).random()
                }
                const2 == 1 -> {
                    a = (najmanji1..najveci1).random()
                    b = najmanji2
                }
                else -> {
                    a = (najmanji1..najveci1).random()
                    b = (najmanji2..najveci2).random()
                }
            }

            if (rnd == "-") {
                b = (najmanji2..najveci1).random()
                a = (b..najveci1).random()
            }

            if (rnd == "/") {
                b = (najmanji2..najveci2).random().coerceAtLeast(1)
                a = b * (1..10).random()
            }



            rez1 = when (rnd) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> 0
            }
        }

        if (round1) a = (a + 9) / 10 * 10
        if (round2) b = (b + 9) / 10 * 10

        prvizad1 = "$a $rnd $b ="
    }
    private fun generator() {
        rez1 = rez1
        prvizad1 = prvizad1
    }

    private fun saveinput(): MathProblem {
        val odgovor = if (rez1 == result.toInt()) "Točno!" else "Rješenje je $rez1"
        val problem = MathProblem("Zadatak $brojaczad\n", prvizad1, "$result\n", "$odgovor\n\n")
        mathProblems.add(problem)
        return problem
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::cdtimer.isInitialized) cdtimer.cancel()
    }

}
