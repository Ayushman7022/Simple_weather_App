package com.example.apicalling

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofit: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
    val api: CityApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CityApiService::class.java)
    }
}
