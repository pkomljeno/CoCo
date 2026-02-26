package com.example.coco

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment

class FragmentAccountChoice : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.guest_or_acc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gostButton = view.findViewById<ImageFilterView>(R.id.gost)
        val racunButton = view.findViewById<ImageFilterView>(R.id.racun)
        val backbtn = view.findViewById<ImageFilterView>(R.id.back)

        backbtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        gostButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, GuestLoginFragment())
                .addToBackStack(null)
                .commit()
        }

        }


    }
