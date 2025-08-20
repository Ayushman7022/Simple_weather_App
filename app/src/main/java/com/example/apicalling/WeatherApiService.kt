package com.example.apicalling

import retrofit2.Response

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {
    @GET("{location}")
    suspend fun getWeatherData(
        @Path("location") location: String,
        @Query("key") apiKey: String
    ): Response<ResponseBody>


}