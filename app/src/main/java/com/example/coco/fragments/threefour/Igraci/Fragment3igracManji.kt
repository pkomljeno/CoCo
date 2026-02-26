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


class Fragment3igracManji : Fragment(R.layout.fragment_fragment3i4igraca) {

    var m2here = ""
    var counter3 = 0

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
        textview = binding.bokime
        textview2 = binding.unutar
        val bckg = binding.cons1

        var parentActivity = activity as Explain4

        val potvrdibtn = binding.check2
        val sharedPref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        val ETtimer = sharedPref.getInt("timer", 5)
        val ime = sharedPref.getString("ime3","Default")
        sharedPref.edit().putInt("counterManji3", 0).apply()

        textview.text = "Bok,\n${ime ?: "Unknown"}"

        textview2.text = "Rotiraj ekran ako ti ne odgovara,\nte potvrdi klikom na kvačicu."

        val rotateButton = binding.rotate
        val contentContainer: View = binding.subview

        textview.setTextColor(Color.parseColor("#98245B"))
        textview2.setTextColor(Color.parseColor("#98245B"))
        potvrdibtn.setBackgroundResource(R.drawable.check_3_)
        rotateButton.setBackgroundResource(R.drawable.vector3)

        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter3manji += 1

            sharedPref.edit().putInt("counterManji3", parentActivity.counter3manji).apply()
        }

        bckg.setBackgroundResource(R.color.trdback)

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

