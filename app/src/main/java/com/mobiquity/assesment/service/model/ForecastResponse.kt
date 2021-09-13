package com.mobiquity.assesment.service.model

import com.google.gson.annotations.SerializedName

data class FiveDaysForecastResponse(val list: ArrayList<ForecastResponse>)

data class ForecastResponse(
    val weather: ArrayList<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    @SerializedName("dt_txt")
    val date: String
)

data class Weather(val id: Long, val main: String, val description: String, val icon: String)

data class Main(
    val temp: Float,
    @SerializedName("temp_min")
    val minTemp: Float,
    @SerializedName("temp_max")
    val maxTemp: Float,
    val pressure: Int,
    val humidity: Int
)

data class Wind(val speed: Float)

data class Sys(val sunrise: Long, val sunset: Long)