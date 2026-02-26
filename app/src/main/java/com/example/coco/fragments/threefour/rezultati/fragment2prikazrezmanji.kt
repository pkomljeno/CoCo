package com.example.coco.fragments.threefour.rezultati

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.Explain4
import com.example.coco.R
import com.example.coco.databinding.ActivityPrikazRezultata2Binding


class fragment2prikazrezmanji : Fragment(R.layout.activity_prikaz_rezultata2) {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var binding: ActivityPrikazRezultata2Binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityPrikazRezultata2Binding.inflate(inflater,container,false)

        var parentActivity = activity as Explain4
        val checkbtn = binding.checkbtn
        val bckg = binding.main
        val contentContainer: View = binding.overview.findViewById(R.id.overview)
        contentContainer.rotation = parentActivity.counter2manji * 90f

        val sharedpref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        var counter22 = parentActivity.counter2manji

        var scrollview = binding.scrollView2
        val defaultHeight = scrollview.layoutParams.height
        val ime1 = binding.imeKut.findViewById<TextView>(R.id.imeKut)
        val rezultati = binding.XodY.findViewById<TextView>(R.id.XodY)
        bckg.setBackgroundResource(R.color.scndback)
        scrollview.setBackgroundResource(R.color.scndrezback)

        val height = 340
        val layoutParams = scrollview.layoutParams


        ime1.text = sharedpref.getString("ime2","Default").toString()

        var tocni = sharedpref.getInt("tocan2", 1)
        var ukupan = sharedpref.getInt("ukupan2", 1)
        val zadaci = sharedpref.getString("zadaci2", "default")

        rezultati.text = "$tocni od $ukupan"

        val textView27 = binding.textView27.findViewById<TextView>(R.id.textView27)
        textView27.text = zadaci


        val rotateButton = binding.rotate
        rotateButton.setBackgroundResource(R.drawable.vector2)
        checkbtn.setBackgroundResource(R.drawable.check_1_)
        textView27.setTextColor(Color.parseColor("#02677E"))

        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter2manji += 1

            counter22 +=1
            if (counter22%2 != 0){
                layoutParams.height = height
                scrollview.layoutParams = layoutParams
                scrollview.requestLayout()

            }

            else {
                val height = defaultHeight
                val layoutParams = scrollview.layoutParams
                layoutParams.height = height
                scrollview.layoutParams = layoutParams
                scrollview.requestLayout()
            }
        }

        checkbtn.setOnClickListener {
            parentActivity.gotoend()

        }


        return binding.root
    }

}
