package com.example.coco
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.constraintlayout.utils.widget.ImageFilterView

class postavke : AppCompatActivity() {

    private var round1 = false
    private var round2 = false
    private var const1 = 0
    private var const2 = 0
    private var firstbig = false

    private fun getInt(edit: EditText, default: Int = 0): Int =
        edit.text.toString().toIntOrNull() ?: default

    fun setVisibility(visible: Boolean, vararg views: View) {
        views.forEach { it.visibility = if (visible) View.VISIBLE else View.GONE }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.postavke)


        //Values

        val sharedpref = getSharedPreferences("values", Context.MODE_PRIVATE)
        val savedamount = sharedpref.getInt("amount", 1)
        val const1load = sharedpref.getInt("const1", 0)
        val const2load = sharedpref.getInt("const2", 0)
        val firstbigload = sharedpref.getBoolean("firstbig", false)
        val rnd1load = sharedpref.getBoolean("round1",false)
        val rnd2load = sharedpref.getBoolean("round2",false)
        val plusload = sharedpref.getBoolean("plusbool",false)
        val minusload = sharedpref.getBoolean("minusbool",false)
        val putaload = sharedpref.getBoolean("putabool",false)
        val dijelload = sharedpref.getBoolean("dijeljbool",false)
        val rasponload = sharedpref.getBoolean("rasponbool",false)
        val istizadload = sharedpref.getBoolean("istizadbool",false)
        var plusbool = true
        var minusbool = false
        var putabool = false
        var dijeljbool = false
        var rasponbool = false
        var istizadbool = false
        var playeramount = savedamount


        val backbtn = findViewById<ImageFilterView>(R.id.back)
        val onepl = findViewById<ImageFilterView>(R.id.p1)
        val twopl = findViewById<ImageFilterView>(R.id.p2)
        val threepl = findViewById<ImageFilterView>(R.id.p3)
        val fourpl = findViewById<ImageFilterView>(R.id.p4)
        val gasi1 = findViewById<ToggleButton>(R.id.toggleButton5)
        val gasi2 = findViewById<ToggleButton>(R.id.toggleButton9)
        val desetice1 = findViewById<ToggleButton>(R.id.toggleButton6)
        val desetice2 = findViewById<ToggleButton>(R.id.toggleButton10)
        val timer = findViewById<EditText>(R.id.vrijemeedit)
        val najmanji1 = findViewById<EditText>(R.id.editimenajmanji)
        val najmanji2 = findViewById<EditText>(R.id.editimenajmanji2)
        var najveci1 = findViewById<EditText>(R.id.editimenajveci)
        var najveci2 = findViewById<EditText>(R.id.editimenajveci2)
        val operand1 = findViewById<TextView>(R.id.textView23)
        val operand2 = findViewById<TextView>(R.id.textView22)
        val najmanjiop1 = findViewById<TextView>(R.id.textView6)
        val najmanjiop2 = findViewById<TextView>(R.id.textView7)
        val plus = findViewById<ToggleButton>(R.id.toggleButton)
        val minus = findViewById<ToggleButton>(R.id.toggleButton2)
        val puta = findViewById<ToggleButton>(R.id.toggleButton3)
        val dijelj = findViewById<ToggleButton>(R.id.toggleButton4)
        val prviveci = findViewById<ToggleButton>(R.id.prviveci)
        val potvrdi = findViewById<ImageFilterView>(R.id.potvrdipos)
        val raspon = findViewById<ToggleButton>(R.id.toggleButton7)
        val istizad = findViewById<ToggleButton>(R.id.istizad)
        val minrangeUI = findViewById<EditText>(R.id.minrange)
        val maxrangeUI = findViewById<EditText>(R.id.maxrange)
        val raspontext = findViewById<TextView>(R.id.textView28)


        //Setting button appearance

        when (playeramount) {
            1 -> {onepl.setImageResource(R.drawable.button_4_)
                twopl.setImageResource(R.drawable.button_1_)
                threepl.setImageResource(R.drawable.button_2_)
                fourpl.setImageResource(R.drawable.button_3_)}

            2 -> {
                onepl.setImageResource(R.drawable.button)
                twopl.setImageResource(R.drawable.button_5_)
                threepl.setImageResource(R.drawable.button_2_)
                fourpl.setImageResource(R.drawable.button_3_)}

            3 -> {
                onepl.setImageResource(R.drawable.button)
                twopl.setImageResource(R.drawable.button_1_)
                threepl.setImageResource(R.drawable.button_6_)
                fourpl.setImageResource(R.drawable.button_3_)
            }

            4 -> {
                onepl.setImageResource(R.drawable.button)
                twopl.setImageResource(R.drawable.button_1_)
                threepl.setImageResource(R.drawable.button_2_)
                fourpl.setImageResource(R.drawable.button_7_)

            }

            else -> {
                print("x is neither 1 nor 2")
            }
        }

            if (const1load == 1 ) {
                najveci1.visibility = View.GONE
                operand1.visibility = View.GONE
                najmanjiop1.text = "Upišite konstantu"
                najmanjiop1.hint = "konstanta"
                gasi1.isChecked = true
                gasi2.isChecked = false
                const1 = 1



            } else {
                najveci1.visibility = View.VISIBLE
                operand1.visibility = View.VISIBLE
                najmanjiop1.text = "Upišite najmanji operand"
                najmanjiop1.hint = "najmanji"
                const1 = 0

            }


        if (const2load == 1) {
            najveci2.visibility = View.GONE
            najmanjiop2.visibility = View.GONE
            operand2.text = "Upišite konstantu"
            operand2.hint = "konstanta"
            gasi1.isChecked = false
            gasi2.isChecked = true
            const2 = 1
        }
        else {
            najveci2.visibility = View.VISIBLE
            najmanjiop2.visibility = View.VISIBLE
            operand2.text = "Upišite najmanji operand"
            operand2.hint = "najmanji"
            const2 = 0

        }

        if (rnd1load){
            desetice1.isChecked = true
            round1 = true

        }
        else {
            round1 = false
            desetice1.isChecked = false
        }

        if (rnd2load){
            desetice2.isChecked = true
            round2 = true
        }
        else {
            round2 = false
            desetice2.isChecked = false
        }

        if (firstbigload){
            prviveci.isChecked = true
            firstbig = true
        }
        else {
            firstbig = false
            prviveci.isChecked = false
        }

        if (plusload){
            plus.isChecked = true
            plusbool = true
        }
        else {
            plusbool = false
            plus.isChecked = false
        }

        if (minusload){
            minus.isChecked = true
            minusbool = true
        }
        else {
            minusbool = false
            minus.isChecked = false
        }

        if (putaload){
            puta.isChecked = true
            putabool = true
        }
        else {
            putabool = false
            puta.isChecked = false
        }


        if (dijelload){
            dijelj.isChecked = true
            dijeljbool = true
        }
        else {
            dijeljbool = false
            dijelj.isChecked = false
        }

        if (rasponload){

            setVisibility(true, minrangeUI, maxrangeUI, raspontext)
            rasponbool = true
            raspon.isChecked = true
        }

        else
            {
                setVisibility(false, minrangeUI, maxrangeUI, raspontext)
                rasponbool = false
                raspon.isChecked = false
            }

        if (istizadload){


            istizadbool = true
            istizad.isChecked = true
        }

        else
        {

            istizadbool = false
            istizad.isChecked = false
        }




        //loading variables

        val savedTimer = sharedpref.getInt("timer", 5)
        val savednajm1 = sharedpref.getInt("najm1", 5)
        val savednajm2 = sharedpref.getInt("najm2", 5)
        val savednajv1 = sharedpref.getInt("najv1", 5)
        val savednajv2 = sharedpref.getInt("najv2", 5)
        val savedminrange = sharedpref.getInt("minrange" , 5)
        val savedmaxrange = sharedpref.getInt("maxrange" , 5)



        timer.setText(savedTimer.toString())
        najmanji1.setText(savednajm1.toString())
        najmanji2.setText(savednajm2.toString())
        najveci1.setText(savednajv1.toString())
        najveci2.setText(savednajv2.toString())
        minrangeUI.setText(savedminrange.toString())
        maxrangeUI.setText(savedmaxrange.toString())


        onepl.setOnClickListener {
            playeramount = 1
            onepl.setImageResource(R.drawable.button_4_)
            twopl.setImageResource(R.drawable.button_1_)
            threepl.setImageResource(R.drawable.button_2_)
            fourpl.setImageResource(R.drawable.button_3_)
        }

        twopl.setOnClickListener {
            playeramount = 2
            onepl.setImageResource(R.drawable.button)
            twopl.setImageResource(R.drawable.button_5_)
            threepl.setImageResource(R.drawable.button_2_)
            fourpl.setImageResource(R.drawable.button_3_)
        }

        threepl.setOnClickListener {
            playeramount = 3
            onepl.setImageResource(R.drawable.button)
            twopl.setImageResource(R.drawable.button_1_)
            threepl.setImageResource(R.drawable.button_6_)
            fourpl.setImageResource(R.drawable.button_3_)
        }

        fourpl.setOnClickListener {
            playeramount = 4
            onepl.setImageResource(R.drawable.button)
            twopl.setImageResource(R.drawable.button_1_)
            threepl.setImageResource(R.drawable.button_2_)
            fourpl.setImageResource(R.drawable.button_7_)
        }

        raspon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                minrangeUI.visibility = View.VISIBLE
                maxrangeUI.visibility = View.VISIBLE
                raspontext.visibility = View.VISIBLE
                rasponbool = true


            } else {
                minrangeUI.visibility = View.GONE
                raspontext.visibility = View.GONE
                maxrangeUI.visibility = View.GONE
                rasponbool = false


            }
        }

        istizad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {


                istizadbool = true


            } else {

                istizadbool = false


            }
        }

        gasi1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                najveci1.visibility = View.GONE
                operand1.visibility = View.GONE
                najmanjiop1.text = "Upišite konstantu"
                najmanjiop1.hint = "konstanta"
                gasi2.isChecked = false
                const1 = 1


            } else {
                najveci1.visibility = View.VISIBLE
                operand1.visibility = View.VISIBLE
                najmanjiop1.text = "Upišite najmanji operand"
                najmanjiop1.hint = "najmanji"
                const1 = 0

            }
        }


        gasi2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                najveci2.visibility = View.GONE
                najmanjiop2.visibility = View.GONE
                operand2.text = "Upišite konstantu"
                operand2.hint = "konstanta"
                gasi1.isChecked = false
                const2 = 1
            }
            else {
                najveci2.visibility = View.VISIBLE
                najmanjiop2.visibility = View.VISIBLE
                operand2.text = "Upišite najmanji operand"
                operand2.hint = "najmanji"
                const2 = 0

            }
        }

        val editor = sharedpref.edit()

        plus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                plusbool = true
                editor.putString("plus", "+")
            }
            else {
                plusbool = false
                editor.putString("plus", "")
            }

        }

        minus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                minusbool = true
                editor.putString("minus", "-")
            }
            else {
                minusbool = false
                editor.putString("minus", "")
            }

        }

        puta.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                putabool = true
                editor.putString("puta", "*")
            }
            else {
                putabool = false
                editor.putString("puta", "")
            }

        }

        dijelj.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                dijeljbool = true
                editor.putString("dijelj", "/")
            }
            else {
                dijeljbool = false
                editor.putString("dijelj", "")
            }

        }



        desetice1.setOnCheckedChangeListener { _, isChecked ->
            round1 = isChecked
        }

        desetice2.setOnCheckedChangeListener { _, isChecked ->
            round2 = isChecked
        }

        prviveci.setOnCheckedChangeListener { _, isChecked ->
            firstbig = isChecked
        }

        potvrdi.setOnClickListener { backbtn.performClick() }


        backbtn.setOnClickListener {

            // Basic validation for main fields
            if (najmanji1.text.isEmpty() || najmanji2.text.isEmpty() || najveci1.text.isEmpty() || najveci2.text.isEmpty() || timer.text.isEmpty()) {
                Toast.makeText(applicationContext, "Unesite brojeve", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Additional validation for raspon fields if rasponbool is true
            if (rasponbool && (minrangeUI.text.isEmpty() || maxrangeUI.text.isEmpty())) {
                Toast.makeText(applicationContext, "Unesite raspon", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare Intent
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PlayerAmount", playeramount)
            val sharedPref = getSharedPreferences("playerAmount", Context.MODE_PRIVATE)
            sharedPref.edit().putInt("playerAmount", playeramount).apply()

            if (const1 == 1) {
                najveci1 = najmanji1
            }
            if (const2 == 1) {
                najveci2 = najmanji2
            }

            val najm1INT = getInt(najmanji1)
            val najm2INT = getInt(najmanji2)
            val najv1INT = getInt(najveci1)
            val najv2INT = getInt(najveci2)
            val timerINT = getInt(timer, 5)
            val minrangeINT = getInt(minrangeUI)
            val maxrangeINT = getInt(maxrangeUI)

            if (najm1INT in 0..1000 && najm2INT in 0..1000
                && najv1INT in 0..1000 && najv2INT in 0..1000
                && najm1INT <= najv1INT && najm2INT <= najv2INT) {

                editor.putInt("timer", timerINT)
                editor.putInt("amount", playeramount)
                editor.putInt("najm1", najm1INT)
                editor.putInt("najm2", najm2INT)
                editor.putInt("najv1", najv1INT)
                editor.putInt("najv2", najv2INT)
                editor.putInt("minrange", minrangeINT)
                editor.putInt("maxrange", maxrangeINT)
                editor.putInt("const1", const1)
                editor.putInt("const2", const2)
                editor.putBoolean("round1", round1)
                editor.putBoolean("round2", round2)
                editor.putBoolean("firstbig", firstbig)
                editor.putBoolean("plusbool", plusbool)
                editor.putBoolean("minusbool", minusbool)
                editor.putBoolean("putabool", putabool)
                editor.putBoolean("dijeljbool", dijeljbool)
                editor.putBoolean("rasponbool", rasponbool)
                editor.putBoolean("istizadbool", istizadbool)
                editor.apply()

                startActivity(intent)
            }
        }

    }

}