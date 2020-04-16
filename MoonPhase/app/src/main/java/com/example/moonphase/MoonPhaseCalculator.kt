package com.example.moonphase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.Integer.parseInt
import java.time.LocalDate
import java.util.*

class MoonPhaseCalculator {

    fun getMoonPhase(year: Int,month: Int, day: Int, algorithm: String): Double{
        if(algorithm.equals("Simple"))
            return Simple(year, month, day)
        if(algorithm.equals("Conway"))
            return Conway(year, month, day)
        else
            return Trig2(year, month, day)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun previousNewMoon(algorithm: String, today: LocalDate): LocalDate {
        var tmpDate: LocalDate = today
        var phase = this.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)

        while(phase != 0.0){
            tmpDate = tmpDate.minusDays(1)
            phase = this.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)
        }
        return tmpDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextFullMoon(algorithm: String, today: LocalDate): LocalDate {
        var tmpDate: LocalDate = today

        var phase = this.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)

        while(phase != 15.0){
            tmpDate = tmpDate.plusDays(1)
            phase = this.getMoonPhase(tmpDate.year, tmpDate.monthValue, tmpDate.dayOfMonth, algorithm)


        }
        Log.d("STATE", tmpDate.toString())
        return tmpDate
    }


    fun Simple(year: Int,month: Int, day: Int): Double {
        var lp = 2551443;
        var now = Date(year,month-1,day,20,35,0);
        var new_moon = Date(1970, 0, 7, 20, 35, 0);
        var phase = ((now.getTime() - new_moon.getTime())/1000) % lp;
        return Math.floor((phase /(24*3600)).toDouble()) + 1;
    }


    fun julday(year1: Int, month1: Int, day1: Int): Double {
        var year = year1
        var month = month1
        var day = day1
        if (year < 0) { year ++; }
        var jy = year;
        var jm = month +1;
        if (month <= 2) {jy--; jm += 12; }
        var jul = Math.floor(365.25 *jy) + Math.floor(30.6001 * jm) + day + 1720995;
        if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
            var ja = Math.floor(0.01 * jy);
            jul = jul + 2 - ja + Math.floor(0.25 * ja);
        }
        return jul;
    }

    fun Trig2(year: Int, month: Int, day: Int): Double {
        var n = Math.floor(12.37 * (year -1900 + ((1.0 * month - 0.5)/12.0)));
        var RAD = 3.14159265/180.0;
        var t = n / 1236.85;
        var t2 = t * t;
        var vAs = 359.2242 + 29.105356 * n;
        var am = 306.0253 + 385.816918 * n + 0.010730 * t2;
        var xtra = 0.75933 + 1.53058868 * n + ((1.178e-4) - (1.55e-7) * t) * t2;
        xtra += (0.1734 - 3.93e-4 * t) * Math.sin(RAD * vAs) - 0.4068 * Math.sin(RAD * am);
        var i = 0
        if (xtra > 0.0){
            i = Math.floor(xtra).toInt()
        }
        else
            i = Math.ceil(xtra - 1.0).toInt()
        var j1 = julday(year,month,day);
        var jd = (2415020 + 28 * n) + i;
        return (j1-jd + 30)%30;
    }

    fun Conway(year: Int, month: Int, day: Int): Double
    {
        var r: Double = year.toDouble() % 100;
        r %= 19;
        if (r>9){ r -= 19;}
        r = ((r * 11) % 30) + month + day;
        if (month<3){r += 2;}
        if(year<2000)
            r-= 4
        else
            r-=8.3
        r = Math.floor(r+0.5)%30
        if (r < 0)
            return r+30
        else
            return r
    }
}