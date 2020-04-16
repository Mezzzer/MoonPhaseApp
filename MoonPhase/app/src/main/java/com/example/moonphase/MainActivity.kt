package com.example.moonphase

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate


class MainActivity : AppCompatActivity() {

    val moonPhaseCalculator: MoonPhaseCalculator = MoonPhaseCalculator()

    var algorithm = "Trigonometric"

    var hemisphere = "N"

    var phase = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    var today: LocalDate = LocalDate.parse("2013-09-04")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSettings.setOnClickListener{
            //saveSettings("hemi", hemisphere)
            //saveSettings("algorithm", algorithm)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        buttonMoon.setOnClickListener{
            startActivity(Intent(this, FullMoonActivity::class.java))
        }

        loadPreferences()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        today = LocalDate.now()

        //setting text
        phase = moonPhaseCalculator.getMoonPhase(today.year, today.monthValue, today.dayOfMonth, algorithm)
        this.textToday.setText("Dzisiaj: " + ((phase/30)*100).toInt().toString() + "%")
        this.textLast.setText("Poprzedni nów: " + moonPhaseCalculator.previousNewMoon(algorithm, today).toString())
        this.textNext.setText("Następna pełnia: "  + moonPhaseCalculator.nextFullMoon(algorithm, today).toString())

        //setting image of moon
        val name: String
        if(hemisphere.equals("N"))
            name = hemisphere.toLowerCase() + (29-phase).toInt().toString()
        else
            name = hemisphere.toLowerCase() + phase.toInt().toString()

        val resID: Int = getResources().getIdentifier(name, "drawable", getPackageName())

        this.imageView.setImageResource(resID)

    }




    fun loadPreferences(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if(sharedPreferences.contains("algorithm")) {
            val algo: String? = sharedPreferences.getString("algorithm", null)
            algorithm = algo.toString()
        }

        if(sharedPreferences.contains("hemi")) {
            hemisphere = sharedPreferences.getString("hemi", null).toString()
        }
    }


    fun saveSettings(key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }
}
