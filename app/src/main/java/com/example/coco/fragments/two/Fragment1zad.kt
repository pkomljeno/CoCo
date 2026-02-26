package com.example.coco.fragments.two

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
import com.example.coco.IstekloVrijeme
import com.example.coco.R
import com.example.coco.Zadatak1Fragment
import com.example.coco.Zadatak1Fragment.Companion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID
import kotlin.random.Random

class Fragment1zad : Fragment() {

    data class MathProblem(val zad: String?, val problem: String?, val solution: String?, val odgovor: String?)
    val mathProblems = arrayListOf<MathProblem>()

    private lateinit var rng: Random



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
        return inflater.inflate(R.layout.fragment_fragment1zad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = activity as Explain2
        val contentContainer: View = view.findViewById(R.id.subview2)
        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val firestore = Firebase.firestore
        val tabletId = getTabletId(requireContext())
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
        val username = sharedpref.getString("ime1", "unknown") ?: "unknown"
        val fixedPlayerId = "player1"
        val playerDocId = sharedpref.getString("${fixedPlayerId}_docId", null)

        val zadload = sharedpref.getBoolean("istizadbool", false)

        rng = if (zadload) {
            val seed = sharedpref.getLong("task_seed", System.currentTimeMillis())
            Random(seed)
        } else {
            Random(System.currentTimeMillis())
        }


        // Load preferences
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

        spawner()
        generator()

        val prvi = view.findViewById<TextView>(R.id.textView20)
        val gotovo = view.findViewById<TextView>(R.id.textView24)
        val unos = view.findViewById<EditText>(R.id.unos)
        val textViewIme = view.findViewById<TextView>(R.id.imeKut)
        val timerView = view.findViewById<TextView>(R.id.vrijeme)
        val ime2 = sharedpref.getString("ime1", null)
        textViewIme.text = ime2
        unos.isFocusable = false

        result = ""
        total = ""
        unos.setText(result)

        val RealETtimer = sharedpref.getInt("timer", 5).toLong() * 60_000
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

                // Update top-level session document
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
                gotovo.rotation = parentActivity.counter1 * 90f
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

        //  ROTATION LOGIC

        val rotateButton: ImageFilterView = view.findViewById(R.id.rotate)
        val savedCounter = sharedpref.getInt("counter1", 0)
        if (parentActivity != null) {
            parentActivity.counter1 = savedCounter
            contentContainer.rotation = parentActivity.counter1 * 90f
            rotateButton.setOnClickListener {
                contentContainer.rotation += 90f
                parentActivity.counter1 += 1
                sharedpref.edit().putInt("counter1", parentActivity.counter1).apply()
            }
        } else {
            Log.e("Fragment1zad", "Parent activity is not Explain2")
        }
    }

    private fun spawner() {
        val symbolsArray = mutableListOf<String>()
        if (plusbool) symbolsArray.add("+")
        if (minusbool) symbolsArray.add("-")
        if (putabool) symbolsArray.add("*")
        if (dijeljbool) symbolsArray.add("/")

        if (symbolsArray.isEmpty()) symbolsArray.add("+") // safeguard

        // Load prefs
        val sharedpref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val useRange = sharedpref.getBoolean("rasponbool", false)
        val minRange = sharedpref.getInt("minrange", 0)
        val maxRange = sharedpref.getInt("maxrange", 100)

        val minA = najmanji1
        val maxA = najveci1
        val minB = najmanji2
        val maxB = najveci2

        val rnd = symbolsArray.random(rng)


        fun applyRoundingAndClamp() {
            if (round1) a = ((a + 9) / 10) * 10
            if (round2) b = ((b + 9) / 10) * 10
            a = a.coerceIn(minA, maxA)
            b = b.coerceIn(minB, maxB)
            if (firstbig && a < b) {
                val temp = a
                a = b
                b = temp
            }
        }

        when (rnd) {
            "+" -> {
                if (useRange) {
                    var valid = false
                    while (!valid) {
                        rez1 = rng.nextInt(maxRange - minRange + 1) + minRange
                        a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                        b = rez1 - a
                        if (b in minB..maxB) valid = true
                    }
                } else {
                    a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                    b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                }
                applyRoundingAndClamp()
                rez1 = a + b
            }

            "-" -> {
                if (useRange) {
                    var valid = false
                    var attempts = 0
                    while (!valid && attempts < 50) {
                        attempts++
                        a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                        val bMin = maxOf(minB, a - maxRange)
                        val bMax = minOf(maxB, a - minRange)
                        if (bMin <= bMax) {
                            b = if (const2 == 1) najmanji2.coerceIn(bMin, bMax) else rng.nextInt(bMax - bMin + 1) + bMin
                            rez1 = a - b
                            valid = true
                        }
                    }
                    if (!valid) {
                        a = minA
                        b = minB.coerceAtMost(a - minRange)
                        rez1 = (a - b).coerceIn(minRange, maxRange)
                    }
                } else {
                    a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                    b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                    if (a < b) {
                        val temp = a
                        a = b
                        b = temp
                    }
                }
                applyRoundingAndClamp()
                rez1 = a - b
            }

            "*" -> {
                if (useRange) {
                    var valid = false
                    while (!valid) {
                        rez1 = rng.nextInt(maxRange - minRange + 1) + minRange
                        val divisors = (1..rez1).filter { rez1 % it == 0 && it in minA..maxA && (rez1 / it) in minB..maxB }
                        if (divisors.isNotEmpty()) {
                            a = divisors[rng.nextInt(divisors.size)]
                            b = rez1 / a
                            valid = true
                        } else {
                            a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                            b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                            rez1 = a * b
                            valid = rez1 in minRange..maxRange
                        }
                    }
                } else {
                    a = if (const1 == 1) najmanji1 else rng.nextInt(maxA - minA + 1) + minA
                    b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                }
                applyRoundingAndClamp()
                rez1 = a * b
            }

            "/" -> {
                if (useRange) {
                    var valid = false
                    while (!valid) {
                        b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                        if (b == 0) b = 1
                        rez1 = rng.nextInt(maxRange - minRange + 1) + minRange
                        a = rez1 * b
                        if (a in minA..maxA) valid = true
                    }
                } else {
                    b = if (const2 == 1) najmanji2 else rng.nextInt(maxB - minB + 1) + minB
                    if (b == 0) b = 1
                    a = if (const1 == 1) najmanji1 else b * (rng.nextInt(10) + 1)
                }
                applyRoundingAndClamp()
                rez1 = a / b
            }
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
