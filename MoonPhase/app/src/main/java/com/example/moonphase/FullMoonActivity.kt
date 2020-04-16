package com.example.moonphase

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_full_moon.*
import java.time.LocalDate

class FullMoonActivity : AppCompatActivity() {

    val moonPhaseCalculator: MoonPhaseCalculator = MoonPhaseCalculator()

    var year: Int = 2020

    var algorithm: String = "Trigonometric"

    var dates: MutableList<String> = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_moon)

        loadPreferences()

        textYear.setText(year.toString())

        buttonPlus.setOnClickListener(){
            year ++;
            textYear.setText(year.toString())
        }

        buttonMinus.setOnClickListener(){
            year--;
            textYear.setText(year.toString())
        }

        buttonBack.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonEnter.setOnClickListener(){
            dates.clear()

            var tmpDate: LocalDate = LocalDate.parse(year.toString() + "-01-01")

            var phase = this.moonPhaseCalculator.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)

            while(tmpDate.year == year){
                if(phase == 15.0){
                    dates.add(tmpDate.toString())
                }

                tmpDate = tmpDate.plusDays(1)
                phase = this.moonPhaseCalculator.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dates)

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            listFull.adapter = adapter
        }
    }


    fun loadPreferences(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if(sharedPreferences.contains("algorithm")) {
            val algo: String? = sharedPreferences.getString("algorithm", null)
            algorithm = algo.toString()
        }
    }

}
