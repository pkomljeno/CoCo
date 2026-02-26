package com.example.coco.fragments.two


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.R
import com.example.coco.databinding.ActivityKrajBinding



class fragment1kraj() : Fragment(R.layout.activity_kraj) {



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    private lateinit var binding: ActivityKrajBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = ActivityKrajBinding.inflate(inflater,container,false)

        val nazad = binding.backtext.findViewById<TextView>(R.id.backtext)
        val nazad2 = binding.backbtn.findViewById<ImageFilterView>(R.id.backbtn)
        val home = binding.zavrsibtn.findViewById<ImageFilterView>(R.id.zavrsibtn)
        var parentActivity = activity as Explain2


        nazad.setOnClickListener {
            parentActivity.goback()
        }

        nazad2.setOnClickListener {
            parentActivity.goback()

        }

        home.setOnClickListener {

        }


        return binding.root
    }

}

