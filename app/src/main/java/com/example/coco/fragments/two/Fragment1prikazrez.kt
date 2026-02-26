package com.example.coco.fragments.threefour.zadaci

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.R
import com.example.coco.databinding.ActivityPrikazRezultata2Binding
import com.example.coco.databinding.ActivityPrikazRezultataBinding


class fragment1prikazrez : Fragment(R.layout.activity_prikaz_rezultata) {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var binding: ActivityPrikazRezultataBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityPrikazRezultataBinding.inflate(inflater,container,false)

        var parentActivity = activity as Explain2
        val checkbtn = binding.check2
        val contentContainer: View = binding.overview.findViewById(R.id.overview)
        contentContainer.rotation = parentActivity.counter1 * 90f
        val sharedpref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)

        val ime1 = binding.imeKut.findViewById<TextView>(R.id.imeKut)
        val rezultati = binding.XodY.findViewById<TextView>(R.id.XodY)

        ime1.text = sharedpref.getString("ime1","Default").toString()

        var tocni = sharedpref.getInt("tocan1", 1)
        var ukupan = sharedpref.getInt("ukupan1", 1)
        val zadaci = sharedpref.getString("zadaci1", "default")

        val rotateButton = binding.rotate

        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter1 += 1
        }

        rezultati.text = "$tocni od $ukupan"

        val textView27 = binding.textView27.findViewById<TextView>(R.id.textView27)
        textView27.text = zadaci

        checkbtn.setOnClickListener {
            parentActivity.gotoend()
        }

        return binding.root
        }

    }
