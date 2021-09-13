package com.mobiquity.assesment.service.api

import com.mobiquity.assesment.service.model.FiveDaysForecastResponse
import com.mobiquity.assesment.service.model.ForecastResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getTodayForecast(
        latitude: Double,
        longitude: Double
    ) = apiService.getTodayForecast(latitude, longitude)

    override suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double
    ): Response<FiveDaysForecastResponse> = apiService.getFiveDaysForecast(latitude, longitude)
}