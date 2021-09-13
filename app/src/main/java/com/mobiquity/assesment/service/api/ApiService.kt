package com.mobiquity.assesment.service.api

import com.mobiquity.assesment.BuildConfig
import com.mobiquity.assesment.service.model.FiveDaysForecastResponse
import com.mobiquity.assesment.service.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather/")
    suspend fun getTodayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): Response<ForecastResponse>

    @GET("data/2.5/forecast/")
    suspend fun getFiveDaysForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") unit: String = "metric",
    ): Response<FiveDaysForecastResponse>
}