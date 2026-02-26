package com.example.coco.fragments.threefour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.example.coco.Explain2
import com.example.coco.Explain4
import com.example.coco.IstekloVrijeme
import com.example.coco.R
import com.example.coco.fragments.two.Fragment1zad
import com.example.coco.fragments.two.Fragment1zad.Companion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class Fragment1zadManji : Fragment() {

    data class MathProblem(val zad: String?, val problem: String?, val solution: String?, val odgovor: String?)
    private val mathProblems = arrayListOf<MathProblem>()

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
    private var tocancnt = 0
    private var krivicnt = 0
    private var brojaczad = 1
    private lateinit var cdtimer: CountDownTimer

    companion object {
        var rez1 = 0
        var prvizad1 = ""
        var total = ""
        var result = ""
        var a = 0
        var b = 0
    }

    private fun getTabletId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown_device"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_fragment1zadmanji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = activity as Explain4
        val contentContainer: View = view.findViewById(R.id.subview2)
        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val sharedPref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val firestore = Firebase.firestore
        val tabletId = getTabletId(requireContext())
        val username = sharedPref.getString("ime1", "unknown") ?: "unknown"
        val fixedPlayerId = "player1"
        val playerDocId = sharedPref.getString("${fixedPlayerId}_docId", null)

        // Load preferences
        najmanji1 = sharedPref.getInt("najm1", 5)
        najmanji2 = sharedPref.getInt("najm2", 5)
        najveci1 = sharedPref.getInt("najv1", 5)
        najveci2 = sharedPref.getInt("najv2", 5)
        round1 = sharedPref.getBoolean("round1", false)
        round2 = sharedPref.getBoolean("round2", false)
        const1 = sharedPref.getInt("const1", 0)
        const2 = sharedPref.getInt("const2", 0)
        firstbig = sharedPref.getBoolean("firstbig", false)
        plusbool = sharedPref.getBoolean("plusbool", true)
        minusbool = sharedPref.getBoolean("minusbool", false)
        putabool = sharedPref.getBoolean("putabool", false)
        dijeljbool = sharedPref.getBoolean("dijeljbool", false)
        minrange = sharedpref.getInt("minrange", 0)
        maxrange = sharedpref.getInt("maxrange", 100)

        spawner()
        generator()

        val prvi = view.findViewById<TextView>(R.id.textView20)
        val gotovo = view.findViewById<TextView>(R.id.textView24)
        val unos = view.findViewById<EditText>(R.id.unos)
        val textViewIme = view.findViewById<TextView>(R.id.imeKut)
        val timerView = view.findViewById<TextView>(R.id.vrijeme)
        textViewIme.text = username
        unos.isFocusable = false

        result = ""
        total = ""
        unos.setText(result)

        val RealETtimer = sharedPref.getInt("timer", 5).toLong() * 60_000
        cdtimer = object : CountDownTimer(RealETtimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                val text = mathProblems.joinToString("") {
                    "${it.zad}${it.problem}${it.solution}${it.odgovor}"
                }
                sharedpref.edit()
                    .putInt("tocan1", tocancnt)
                    .putInt("ukupan1", tocancnt + krivicnt)
                    .putString("zadaci1", text)
                    .apply()


                val sessionIdNow = sharedpref.getString("sessionId", null)
                if (sessionIdNow != null) {
                    val sessionRef = firestore.collection("activitySessions").document(sessionIdNow)
                    sessionRef.update("endTimestamp", FieldValue.serverTimestamp())
                        .addOnSuccessListener {
                            Log.d("Firestore", "Top-level endTimestamp saved")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to save endTimestamp: ${e.message}")
                        }
                }

                contentContainer.visibility = View.GONE
                gotovo.rotation = parentActivity.counter1manji * 90f
                gotovo.visibility = View.VISIBLE
                parentActivity.nextfragment2 = true
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
            } else {
                val userAnswer = result.toInt()
                val correctAnswer = rez1
                val sessionIdNow = sharedpref.getString("sessionId", null)

                val answerData = hashMapOf(
                    "username" to username,
                    "tabletId" to tabletId,
                    "sessionId" to sessionIdNow,
                    "playerDocId" to playerDocId,
                    "Zadatak" to prvizad1,
                    "Odgovor" to userAnswer,
                    "TočanOdgovor" to correctAnswer,
                    "Točno" to (userAnswer == correctAnswer),
                    "timestamp" to FieldValue.serverTimestamp()
                )

                if (sessionIdNow != null) {
                    val sessionRef = firestore.collection("activitySessions").document(sessionIdNow)


                    sessionRef.collection("answers").add(answerData)
                        .addOnFailureListener {
                            Log.e("Firestore", "Failed to save answer: ${it.message}")
                        }


                    val playerDocId = sharedpref.getString("${fixedPlayerId}_docId", null)

                    if (playerDocId != null) {
                        val playerRef = sessionRef.collection("players").document(playerDocId)

                        val updates: Map<String, Any> = mapOf(
                            "tabletId" to tabletId,
                            "username" to username,
                            "ukupno" to FieldValue.increment(1),
                            "tocni" to if (userAnswer == correctAnswer) FieldValue.increment(1) else FieldValue.increment(0),
                            "netocni" to if (userAnswer != correctAnswer) FieldValue.increment(1) else FieldValue.increment(0),
                            "krajAktivnosti" to FieldValue.serverTimestamp()
                        )

                        playerRef.update(updates)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Updated player stats for $username ($playerDocId)")
                            }
                            .addOnFailureListener {
                                Log.e("Firestore", "Failed to update player stats for $username ($playerDocId)", it)
                            }
                    } else {
                        Log.e("Firestore", "Missing playerDocId in SharedPreferences for $username")
                    }
                }


                else {
                    Log.e("Firestore", "Session ID is null")
                }

                saveinput()

                if (userAnswer == correctAnswer) tocancnt++ else krivicnt++
                brojaczad++

                result = ""
                total = ""
                unos.setText("")
                spawner()
                generator()
                prvi.text = prvizad1
            }
        }

        prvi.text = prvizad1

        // rotation
        val rotateButton: ImageFilterView = view.findViewById(R.id.rotate)
        val savedCounter = sharedPref.getInt("counterManji1", 0)
        if (parentActivity != null) {
            parentActivity.counter1manji = savedCounter
            contentContainer.rotation = parentActivity.counter1manji * 90f
            rotateButton.setOnClickListener {
                contentContainer.rotation += 90f
                parentActivity.counter1manji += 1
                sharedPref.edit().putInt("counterManji1", parentActivity.counter1manji).apply()
            }
        } else {
            Log.e("Fragment1zadManji", "Parent activity is not Explain4")
        }
    }

    private val rng = Random(System.currentTimeMillis())

    private fun spawner() {
        val symbolsArray = mutableListOf<String>()
        if (plusbool) symbolsArray.add("+")
        if (minusbool) symbolsArray.add("-")
        if (putabool) symbolsArray.add("*")
        if (dijeljbool) symbolsArray.add("/")

        if (symbolsArray.isEmpty()) symbolsArray.add("+") // safeguard

        // Load preferences
        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val useRange = sharedpref.getBoolean("rasponbool", false)
        val minRange = sharedpref.getInt("minrange", 0)
        val maxRange = sharedpref.getInt("maxrange", 100)

        val minA = najmanji1
        val maxA = najveci1
        val minB = najmanji2
        val maxB = najveci2

        val rnd = symbolsArray.random(rng)

        when (rnd) {
            "+" -> {
                if (useRange) {
                    rez1 = (minRange..maxRange).random(rng)
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                    b = rez1 - a
                    if (b !in minB..maxB) b = (minB..maxB).random(rng)
                } else {
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                    b = if (const2 == 1) najmanji2 else (minB..maxB).random(rng)
                    rez1 = a + b
                }
            }

            "-" -> {
                if (useRange) {
                    rez1 = (minRange..maxRange).random(rng)
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                    b = a - rez1
                    if (b !in minB..maxB) b = (minB..maxB).random(rng)
                } else {
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                    b = if (const2 == 1) najmanji2 else (minB..maxB).random(rng)
                    if (a < b) { val tmp = a; a = b; b = tmp }
                    rez1 = a - b
                }
            }

            "*" -> {
                if (useRange) {
                    rez1 = (minRange..maxRange).random(rng)
                    val divisors = (1..rez1).filter { rez1 % it == 0 && it in minA..maxA && (rez1 / it) in minB..maxB }
                    if (divisors.isNotEmpty()) {
                        a = divisors.random(rng)
                        b = rez1 / a
                    } else {
                        a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                        b = if (const2 == 1) najmanji2 else (minB..maxB).random(rng)
                        rez1 = a * b
                    }
                } else {
                    a = if (const1 == 1) najmanji1 else (minA..maxA).random(rng)
                    b = if (const2 == 1) najmanji2 else (minB..maxB).random(rng)
                    rez1 = a * b
                }
            }

            "/" -> {
                b = if (const2 == 1) najmanji2.coerceAtLeast(1) else (minB..maxB).filter { it != 0 }.random(rng)
                if (useRange) {
                    rez1 = (minRange..maxRange).random(rng)
                    a = rez1 * b
                    if (a !in minA..maxA) a = (minA..maxA).random(rng)
                } else {
                    a = if (const1 == 1) najmanji1 else b * ((1..10).random(rng))
                    rez1 = a / b
                }
            }
        }


        if (round1) a = ((a + 9) / 10) * 10
        if (round2) b = ((b + 9) / 10) * 10


        if (firstbig && a < b) {
            val tmp = a
            a = b
            b = tmp
        }

        prvizad1 = "$a $rnd $b ="



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


            isEnabled = false
            requireActivity().onBackPressed()
        }

    }

    private fun generator() { /* nothing extra needed */ }

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
