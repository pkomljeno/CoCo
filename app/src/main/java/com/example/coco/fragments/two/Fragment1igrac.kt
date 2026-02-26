package com.example.coco.fragments.two

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.coco.Explain2
import com.example.coco.R
import com.example.coco.databinding.FragmentFragment1i2igracaBinding


class fragment1igrac() : Fragment(R.layout.fragment_fragment1i2igraca) {


    var m2here = ""
    var counter1 = 0
    var playeramount = 0



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    var provjera1 = 0
    private lateinit var binding: FragmentFragment1i2igracaBinding
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


        binding = FragmentFragment1i2igracaBinding.inflate(inflater,container,false)
        textview = binding.AM2.findViewById<TextView>(R.id.AM2)
        textview2 = binding.unutar.findViewById<TextView>(R.id.unutar)
        val bckg = binding.cons1.findViewById(R.id.cons1) as ConstraintLayout

        bckg.setBackgroundResource(R.color.editbckgr)

        val sharedPref = this.requireActivity().getSharedPreferences("values", Context.MODE_PRIVATE)
        playeramount = sharedPref.getInt("amount",1)

        var parentActivity = activity as Explain2
        val potvrdibtn = binding.check2.findViewById<ToggleButton>(R.id.check2)
        val ETtimer = sharedPref.getInt("timer", 5)

        val ime = sharedPref.getString("ime1","Default")


        textview.text = "Bok,\n${ime ?: "Unknown"}"

            textview2.text = "Rotiraj ekran ako ti ne odgovara,\nte potvrdi klikom na kvačicu."


        val rotateButton = binding.rotate
        val contentContainer: View = binding.subview.findViewById(R.id.subview)
        rotateButton.setOnClickListener {
            contentContainer.rotation += 90f
            parentActivity.counter1 += 1
            sharedPref.edit().putInt("counter1", parentActivity.counter1).apply()
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

