package com.example.coco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment


class GuestLoginFragment : Fragment() {

    private var playerAmount: Int = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = requireActivity().getSharedPreferences("playerAmount", Context.MODE_PRIVATE)
        playerAmount = sharedPref.getInt("playerAmount", 1) // default to 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_welcome2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)

        val potvrdibtn = view.findViewById<ImageFilterView>(R.id.potvrdi2)
        val edittext = view.findViewById<EditText>(R.id.editime12pl)
        val edittext2 = view.findViewById<EditText>(R.id.editime22pl)
        val edittext3 = view.findViewById<EditText>(R.id.editime12pl2)
        val edittext4 = view.findViewById<EditText>(R.id.editime22pl2)
        val unesi = view.findViewById<TextView>(R.id.textView19)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.overlay)
        val backbtn = view.findViewById<ImageFilterView>(R.id.back)


        // Adjust visibility
        when (playerAmount) {
            1 -> {
                edittext2.visibility = View.GONE
                edittext3.visibility = View.GONE
                edittext4.visibility = View.GONE
                unesi.visibility = View.GONE
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.centerHorizontally(R.id.editime12pl, ConstraintSet.PARENT_ID)
                constraintSet.applyTo(constraintLayout)
            }
            2 -> {
                edittext3.visibility = View.GONE
                edittext4.visibility = View.GONE
            }
            3 -> {
                edittext4.visibility = View.GONE
            }
        }

        backbtn.setOnClickListener {
            val Intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(Intent)
        }


        // Confirm button logic
        potvrdibtn.setOnClickListener {
            val ime = edittext.text.toString()
            val ime2 = edittext2.text.toString()
            val ime3 = edittext3.text.toString()
            val ime4 = edittext4.text.toString()

            val editor = sharedPref.edit()

            when (playerAmount) {
                1 -> {
                    if (ime.isEmpty()) {
                        Toast.makeText(requireContext(), "Unesite ime", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putString("ime1", ime)
                        editor.apply()
                        goToFragment(Explain1Fragment())
                    }
                }
                2 -> {
                    if (ime.isEmpty() || ime2.isEmpty()) {
                        Toast.makeText(requireContext(), "Unesite imena", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putString("ime1", ime)
                        editor.putString("ime2", ime2)
                        editor.apply()

                        val intent = Intent(requireContext(), Explain2::class.java)
                        startActivity(intent)
                    }
                }
                3 -> {
                    if (ime.isEmpty() || ime2.isEmpty() || ime3.isEmpty()) {
                        Toast.makeText(requireContext(), "Unesite imena", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putString("ime1", ime)
                        editor.putString("ime2", ime2)
                        editor.putString("ime3", ime3)
                        editor.apply()
                        val intent = Intent(requireContext(), Explain4::class.java)
                        startActivity(intent)
                    }
                }
                4 -> {
                    if (ime.isEmpty() || ime2.isEmpty() || ime3.isEmpty() || ime4.isEmpty()) {
                        Toast.makeText(requireContext(), "Unesite imena", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putString("ime1", ime)
                        editor.putString("ime2", ime2)
                        editor.putString("ime3", ime3)
                        editor.putString("ime4", ime4)
                        editor.apply()
                        val intent = Intent(requireContext(), Explain4::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun goToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }



}



