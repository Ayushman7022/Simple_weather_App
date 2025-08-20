package com.example.apicalling

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val apiKey = "W9CJBTFQPY9AW958CZBVA9GET"
    private val TAG = "WeatherAPI"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cityAutoComplete: AutoCompleteTextView

    private val LOCATION_PERMISSION_REQUEST = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityAutoComplete = findViewById(R.id.cityAutoComplete)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()
        setupCitySearch()
    }

    // --- City search ---
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCitySearch() {
        cityAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length > 2) {
                    fetchCitySuggestions(s.toString())
                }
            }
        })
    }

    private fun fetchCitySuggestions(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getCities(query, 5, "ebf3cbcd61b77f322542ff74e36d168e")
                if (response.isSuccessful) {
                    val cities = response.body() ?: emptyList()
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        cities
                    )
                    cityAutoComplete.setAdapter(adapter)
                    cityAutoComplete.showDropDown()

                    cityAutoComplete.setOnItemClickListener { _, _, position, _ ->
                        val selectedCity = cities[position].name
                        fetchWeather(selectedCity)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "City API exception: ${e.message}")
            }
        }
    }

    // --- Weather fetch ---
    private fun fetchWeather(location: String) {
        lifecycleScope.launch {
            try {
                // Pass the location directly (can be city name or "lat,lon")
                val response = RetrofitClient.retrofit.getWeatherData(location, apiKey)

                if (response.isSuccessful && response.body() != null) {
                    val json = response.body()!!.string()
                    Log.d(TAG, json)

                    val weatherResponse = Gson().fromJson(json, WeatherData::class.java)

                    val today = weatherResponse.days[0]  // take only the first day
                    val listView = findViewById<ListView>(R.id.weatherlistview)
                    val adapter = WeatherAdapter(this@MainActivity, listOf(today),weatherResponse.description) // wrap in a list
                    listView.adapter = adapter

                } else {
                    Log.e(TAG, "Weather API error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Weather fetch exception: ${e.message}")
            }
        }
    }


    // --- Location permission ---
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            getUserLocation()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation()
        }
    }

    // --- Get device location ---
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getUserLocation() {
          fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val coordinates = "${location.latitude},${location.longitude}"
                fetchWeather(coordinates)  // ✅ Pass lat,lon directly
            } else {
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
