package com.example.coco

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import java.util.UUID

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var random: kotlin.random.Random
    }

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pocetni_ekran)




        val stanje = findViewById<TextView>(R.id.ispisstanja)
        val stanje2 = findViewById<TextView>(R.id.stanje2)
        val postavke2 = findViewById<ImageFilterView>(R.id.postavke)
        val pokrenibtn = findViewById<ImageFilterView>(R.id.pokrenibtn)
        val sharedpref = getSharedPreferences("values", Context.MODE_PRIVATE)
        val savedamount = sharedpref.getInt("amount", 1)
        val timer = sharedpref.getInt("timer", 1)
        val plus = sharedpref.getString("plus", "")
        val minus = sharedpref.getString("minus", "")
        val puta = sharedpref.getString("puta", "")
        val dijelj = sharedpref.getString("dijelj", "")
        val najm1 = sharedpref.getInt("najm1", 1)
        val najm2 = sharedpref.getInt("najm2", 1)
        val najv1 = sharedpref.getInt("najv1", 1)
        val najv2 = sharedpref.getInt("najv2", 1)
        val zadload = sharedpref.getBoolean("istizadbool", false)
        stanje.text = "Broj učenika: $savedamount | Trajanje: $timer min | Operatori: $plus $minus $puta $dijelj"
        stanje2.text = "Prvi operand: od $najm1 do $najv1 | Drugi operand: od $najm2 do $najv2"

        val sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE)
        sharedPref.edit().remove("sessionId").apply()


        if (zadload) {
            val seed = System.currentTimeMillis()
            sharedPref.edit().putLong("task_seed", seed).apply()
        }


        postavke2.setOnClickListener {
            val intent = Intent(this,postavke::class.java)
            startActivity(intent)
        }

        pokrenibtn.setOnClickListener {
            val intent = Intent(this,Fragmentshower::class.java)
            intent.putExtra("PlayerAmount",savedamount )
            startActivity(intent)
            }
        }




    }