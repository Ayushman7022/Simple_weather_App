package com.example.apicalling

data class WeatherData(
    val description: String,
    val days: List<Day>
)

data class Day(
    val temp:Double,
    val tempmax: Double,
    val tempmin: Double,
    val humidity: Double,
    val conditions: String,

)


