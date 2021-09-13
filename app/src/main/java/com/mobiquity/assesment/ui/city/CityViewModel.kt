package com.mobiquity.assesment.ui.city

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mobiquity.assesment.R
import com.mobiquity.assesment.service.model.ErrorResponse
import com.mobiquity.assesment.service.model.FiveDaysForecastResponse
import com.mobiquity.assesment.service.model.ForecastResponse
import com.mobiquity.assesment.service.repository.MobiquityRepository
import com.mobiquity.assesment.service.utility.NetworkHelper
import com.mobiquity.assesment.service.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val repository: MobiquityRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _todayForecastMutableLiveData by lazy { MutableLiveData<Resource<ForecastResponse>>() }

    private val _fiveDaysForecastMutableLiveData by lazy { MutableLiveData<Resource<FiveDaysForecastResponse>>() }

    var isApiLoadedOnce = false

    val todayForecastLiveData: LiveData<Resource<ForecastResponse>>
        get() = _todayForecastMutableLiveData

    val fiveDaysForecastLiveData: LiveData<Resource<FiveDaysForecastResponse>>
        get() = _fiveDaysForecastMutableLiveData

    fun getTodayForecast(lat: Double, long: Double) {
        with(_todayForecastMutableLiveData) {
            value = Resource.loading(null)
            viewModelScope.launch {
                if (networkHelper.isNetworkConnected()) {
                    val response = repository.getTodayForecast(lat, long)
                    response.takeIf { it.isSuccessful }?.let { res ->
                        res.body()?.let {
                            value = Resource.success(it)
                        } ?: run {
                            value =
                                Resource.error(ctx.getString(R.string.something_went_wrong), null)
                        }
                    } ?: run {
                        response.errorBody()?.let {
                            val errorResponse =
                                Gson().fromJson(it.charStream(), ErrorResponse::class.java)
                            value = Resource.error(errorResponse.message, null)
                        }
                    }
                } else {
                    value = Resource.error(ctx.getString(R.string.something_went_wrong), null)
                }
            }
        }
    }

    fun getFiveDaysForecast(lat: Double, long: Double) {
        with(_fiveDaysForecastMutableLiveData) {
            value = Resource.loading(null)
            viewModelScope.launch {
                if (networkHelper.isNetworkConnected()) {
                    val response = repository.getFiveDaysForecast(lat, long)
                    response.takeIf { it.isSuccessful }?.let { res ->
                        res.body()?.let {
                            value = Resource.success(it)
                        } ?: run {
                            value =
                                Resource.error(ctx.getString(R.string.something_went_wrong), null)
                        }
                    } ?: run {
                        response.errorBody()?.let {
                            val errorResponse =
                                Gson().fromJson(it.charStream(), ErrorResponse::class.java)
                            value = Resource.error(errorResponse.message, null)
                        }
                    }
                } else {
                    value = Resource.error(ctx.getString(R.string.something_went_wrong), null)
                }
            }
        }
    }
}