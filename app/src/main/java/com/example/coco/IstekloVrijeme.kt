package com.example.coco

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class IstekloVrijeme : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isteklo_vrijeme)

        var layout = findViewById<ConstraintLayout>(R.id.constraintLayout32)
        val stringOne = getIntent().getStringExtra("zadaci")

        layout.setOnClickListener{
            val Intent = Intent(this,PrikazRezultata::class.java)
            Intent.putExtra("zadaci",stringOne)
            startActivity(Intent)}

    }
}