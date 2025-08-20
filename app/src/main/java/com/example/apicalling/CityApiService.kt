package com.example.apicalling

import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {
    @GET("geo/1.0/direct")
    suspend fun getCities(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String

    ): Response<List<City>>
}

