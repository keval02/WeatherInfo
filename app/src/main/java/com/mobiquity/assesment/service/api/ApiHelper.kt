package com.mobiquity.assesment.service.api

import com.mobiquity.assesment.service.model.FiveDaysForecastResponse
import com.mobiquity.assesment.service.model.ForecastResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getTodayForecast(latitude: Double, longitude: Double): Response<ForecastResponse>

    suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double
    ): Response<FiveDaysForecastResponse>
}