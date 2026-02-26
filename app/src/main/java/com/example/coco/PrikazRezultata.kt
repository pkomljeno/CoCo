package com.example.coco


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import org.w3c.dom.Text


class PrikazRezultata : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prikaz_rezultata1)

        var zadver = false
        val sharedpref = getSharedPreferences("values", Context.MODE_PRIVATE)


        val checkmark = findViewById<ImageFilterView>(R.id.check2)
        val textView27 = findViewById<TextView>(R.id.textView27)

        var ime1 = findViewById<TextView>(R.id.imeKut)
        ime1.text = sharedpref.getString("ime1","Default").toString()
        var rezultati = findViewById<TextView>(R.id.XodY)
        var tocni = sharedpref.getInt("tocan", 1)
        var ukupan = sharedpref.getInt("ukupan", 1)
        var zadaci = sharedpref.getString("zadaci", "default")

        rezultati.text = "$tocni od $ukupan"

       textView27.text = zadaci


        checkmark.setOnClickListener {
            val Intent = Intent(this,Kraj::class.java)
            startActivity(Intent)

        }
    }
}





