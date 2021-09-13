package com.mobiquity.assesment.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiquity.assesment.service.database.entity.City
import com.mobiquity.assesment.service.repository.MobiquityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MobiquityRepository) : ViewModel() {

    fun getAllCities() = repository.getAllCities()

    fun getCityById(id: Long) = repository.getCityById(id)

    fun addCity(city: City) {
        viewModelScope.launch { repository.addCity(city) }
    }

    fun deleteCityById(id: Long) {
        viewModelScope.launch { repository.deleteCity(id) }
    }
}