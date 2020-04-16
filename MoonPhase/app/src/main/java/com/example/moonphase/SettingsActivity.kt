package com.example.moonphase

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    val algorithms = arrayOf("Trigonometric", "Conway", "Simple")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, algorithms)

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerAlgo.adapter = adapter;


        buttonBack.setOnClickListener {

            if(switchS.isChecked)
                saveSettings("hemi","S")
            else
                saveSettings("hemi", "N")
            saveSettings("algorithm",spinnerAlgo.selectedItem.toString())

            startActivity(Intent(this, MainActivity::class.java))

        }

        loadPreferences()


    }

    fun saveSettings(key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun loadPreferences(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if(sharedPreferences.contains("algorithm")) {
            val algo: String? = sharedPreferences.getString("algorithm", null)
            Log.d("POZYCJA", algorithms.indexOf((algo)).toString())
            spinnerAlgo.setSelection(algorithms.indexOf(algo))
        }

        if(sharedPreferences.contains("hemi")) {
            sharedPreferences.getString("hemi", null).toString()
            if(sharedPreferences.getString("hemi", null).toString().equals("S"))
                switchS.setChecked(true)
        }
    }

    fun getPositionFromSpinner(value: String): Int {
        for (i in 0 until spinnerAlgo.getCount()) {
            if (spinnerAlgo.getItemAtPosition(i).toString().equals(value)) {
                return i
                break
            }
        }
        return 0
    }
}


