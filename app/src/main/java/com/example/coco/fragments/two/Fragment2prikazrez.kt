package com.example.coco.fragments.threefour.zadaci

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.R
import com.example.coco.databinding.ActivityPrikazRezultata2Binding
import com.example.coco.databinding.ActivityPrikazRezultataBinding


class fragment2prikazrez : Fragment(R.layout.activity_prikaz_rezultata) {




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
        contentContainer.rotation = parentActivity.counter2 * 90f
        val sharedpref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)

        val scrollview = binding.scrollView2
        val ime1 = binding.imeKut.findViewById<TextView>(R.id.imeKut)
        val rezultati = binding.XodY.findViewById<TextView>(R.id.XodY)
        contentContainer.setBackgroundResource(R.color.scndback)
        scrollview.setBackgroundResource(R.color.scndrezback)


        ime1.text = sharedpref.getString("ime2","Default").toString()

        var tocni = sharedpref.getInt("tocan2", 1)
        var ukupan = sharedpref.getInt("ukupan2", 1)
        val zadaci = sharedpref.getString("zadaci2", "default")

        val rotateButton = binding.rotate

        rotateButton.setBackgroundResource(R.drawable.vector2)
        checkbtn.setBackgroundResource(R.drawable.check_1_)

        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter2 += 1
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
