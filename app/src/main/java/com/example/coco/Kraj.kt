package com.example.coco

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView

class Kraj : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kraj)

        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)
        var playeramount = sharedPref.getInt("amount", 1)
        val nazad = findViewById<TextView>(R.id.backtext)
        val nazad2 = findViewById<ImageFilterView>(R.id.backbtn)
        val home = findViewById<ImageFilterView>(R.id.zavrsibtn)


       when (playeramount){

           1->{nazad.setOnClickListener {
               val Intent = Intent(this,PrikazRezultata::class.java)
               startActivity(Intent)
                }

               nazad2.setOnClickListener {
                   val Intent = Intent(this,PrikazRezultata::class.java)
                   startActivity(Intent)
               }
           }
           2,3,4->{nazad.setOnClickListener {
                   finish()

           }

               nazad2.setOnClickListener {
                   finish()
               }}

           }

        home.setOnClickListener {
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
        }

    }
}