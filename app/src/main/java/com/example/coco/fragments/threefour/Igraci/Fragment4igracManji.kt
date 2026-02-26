package com.example.coco.fragments.threefour.Igraci

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.Explain4
import com.example.coco.R
import com.example.coco.databinding.FragmentFragment1i2igracaBinding
import com.example.coco.databinding.FragmentFragment3i4igracaBinding


class Fragment4igracManji : Fragment(R.layout.fragment_fragment3i4igraca) {

    var m2here = ""
    var counter4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    var provjera1 = 0
    private lateinit var binding: FragmentFragment3i4igracaBinding
    lateinit var textview: TextView
    lateinit var textview2: TextView
    fun update(m2: String, m1: String) {
        textview2.setText(m2)
        textview.setText(m1)
        m2here = m2

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFragment3i4igracaBinding.inflate(inflater,container,false)
        textview = binding.bokime.findViewById<TextView>(R.id.bokime)
        textview2 = binding.unutar.findViewById<TextView>(R.id.unutar)
        val bckg = binding.cons1

        var parentActivity = activity as Explain4

        val potvrdibtn = binding.check2.findViewById<ToggleButton>(R.id.check2)
        val sharedPref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val ETtimer = sharedPref.getInt("timer", 5)
        val ime = sharedPref.getString("ime4","Default")
        sharedPref.edit().putInt("counterManji4", 0).apply()

        textview.text = "Bok,\n${ime ?: "Unknown"}"

        textview2.text = "Rotiraj ekran ako ti ne odgovara,\nte potvrdi klikom na kvačicu."

        val rotateButton = binding.rotate
        val contentContainer: View = binding.subview.findViewById(R.id.subview)

        textview.setTextColor(Color.parseColor("#946502"))
        textview2.setTextColor(Color.parseColor("#946502"))
        potvrdibtn.setBackgroundResource(R.drawable.check_4_)
        rotateButton.setBackgroundResource(R.drawable.vector4)
        bckg.setBackgroundResource(R.color.frtback)

        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter4manji += 1

            sharedPref.edit().putInt("counterManji4", parentActivity.counter4manji).apply()
        }



        potvrdibtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                when(parentActivity.state2){
                    false->{textview2.text = "Čekaju se svi učenici."
                        textview2.setGravity(Gravity.CENTER);
                        parentActivity.addCheck()
                    }
                    true->{textview2.setGravity(Gravity.CENTER);
                        textview2.text =m2here
                        parentActivity.s2rmvcheck()

                    }
                }
            }

            else {
                when(parentActivity.state2){
                    false->{textview2.text = "Rotiraj ekran ako ti ne odgovara,\nte potvrdi klikom na kvačicu."
                        parentActivity.removeCheck()
                    }
                    true->{textview2.text = "Čekaju se svi učenici."
                        parentActivity.s2addcheck()
                    }
                }
            }
        }


        return binding.root
    }


}

