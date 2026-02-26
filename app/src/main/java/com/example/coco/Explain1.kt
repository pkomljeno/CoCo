package com.example.coco
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class Explain1Fragment : Fragment() {

    private var ime: String? = null
    private var timer: Int = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private fun getTabletId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown_device"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_explain1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        ime = sharedPref.getString("ime1", "Unknown")
        timer = sharedPref.getInt("timer", 3)
        val studentName = ime ?: "Unknown"


        var sessionId = sharedPref.getString("sessionId", null)
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString()
            sharedPref.edit().putString("sessionId", sessionId).apply()

            val tabletId = getTabletId(requireContext())
            val savedAmount = sharedPref.getInt("amount", 1)
            val konfiguracija = "1:$savedAmount"


            val sessionData = hashMapOf(
                "sessionId" to sessionId,
                "tabletId" to tabletId,
                "activityId" to UUID.randomUUID().toString(),
                "timestamp" to FieldValue.serverTimestamp(),
                "endTime" to null,
                "lengthOfActivity" to sharedPref.getInt("timer", 5),
                "correctAnswers" to 0,
                "wrongAnswers" to 0,
                "totalTasks" to 0,
                "Konfiguracija" to konfiguracija,
                "students" to listOf(studentName)
            )

            val sessionRef = Firebase.firestore.collection("activitySessions").document(sessionId)
            val studentName = ime ?: "Unknown"


            sessionRef.set(sessionData)
                .addOnSuccessListener {
                    Log.d("Firebase", "Session created: $sessionId")


                    val playerDocId = UUID.randomUUID().toString()

                    val playerData = hashMapOf(
                        "imeUcenika" to studentName,
                        "tocni" to 0,
                        "netocni" to 0,
                        "ukupno" to 0,
                        "konfiguracija" to konfiguracija,
                        "timestamp" to FieldValue.serverTimestamp(),
                        "krajAktivnosti" to null,
                        "vrijeme" to timer
                    )


                    sessionRef.collection("players").document(playerDocId).set(playerData)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Player doc created with ID: $playerDocId for $studentName")

                            sharedPref.edit().putString("${studentName}_docId", playerDocId).apply()
                        }
                        .addOnFailureListener {
                            Log.e("Firebase", "Failed to create player doc for $studentName", it)
                            Toast.makeText(requireContext(), "Failed to create player doc", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Failed to create session", it)
                    Toast.makeText(requireContext(), "Failed to create session", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("Firebase", "✅ Session already exists: $sessionId")
        }

        // --- UI Setup Section ---
        val greetingText = view.findViewById<TextView>(R.id.AM2)
        val instructionsText = view.findViewById<TextView>(R.id.unutar)
        val potvrdibtn = view.findViewById<ImageFilterView>(R.id.check)

        greetingText.text = "Bok,\n${studentName}"

        instructionsText.text = when (timer) {
            1 -> "Unutar sljedeće minute pokušaj \nriješiti što više zadataka."
            in 2..4 -> "Unutar sljedeće $timer minute pokušaj \nriješiti što više zadataka."
            else -> "Unutar sljedećih $timer minuta pokušaj \nriješiti što više zadataka."
        }

        potvrdibtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, Zadatak1Fragment())
                .addToBackStack(null)
                .commit()
        }
    }



}