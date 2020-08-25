package com.example.tonyphonepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tonyccpicker.CountryInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = CountryInfo.getAllCountries(this, "korean")
        for(country in list){
            Log.e("WONSIK", "${country.name}")
        }
    }
}