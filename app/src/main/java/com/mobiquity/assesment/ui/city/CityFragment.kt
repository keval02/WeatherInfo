package com.mobiquity.assesment.ui.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquity.assesment.R
import com.mobiquity.assesment.base.BaseFragment
import com.mobiquity.assesment.service.model.FiveDaysForecastResponse
import com.mobiquity.assesment.service.model.ForecastResponse
import com.mobiquity.assesment.service.utility.ApiStatus
import com.mobiquity.assesment.ui.home.HomeViewModel
import com.mobiquity.assesment.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*

@AndroidEntryPoint
class CityFragment : BaseFragment() {

    companion object {
        const val CITY_ID_ARG = "cityIdArgument"
    }

    private var cityId: Long? = null
    private lateinit var adapter: FiveDaysForecastAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()

    override fun getLayoutResourceId() = R.layout.fragment_city

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityId = arguments?.getLong(CITY_ID_ARG)
        setAdapter()
        initViewModel()
    }

    private fun setAdapter() {
        adapter = FiveDaysForecastAdapter()
        rvBottomSheet.adapter = adapter
    }

    private fun initViewModel() {
        cityId?.let {
            homeViewModel.getCityById(it).observe(viewLifecycleOwner, { city ->
                setPageTitle(city.cityName)

                cityViewModel.getTodayForecast(
                    city.latitude,
                    city.longitude
                ) // calling today forecast api

                cityViewModel.getFiveDaysForecast(
                    city.latitude,
                    city.longitude
                ) // calling five days forecast api
            })
        }

        cityViewModel.todayForecastLiveData.observe(viewLifecycleOwner, { resources ->
            when (resources.status) {
                ApiStatus.LOADING -> {
                    if (!cityViewModel.isApiLoadedOnce) {
                        showDialog()
                        cityViewModel.isApiLoadedOnce = true
                    }
                }
                ApiStatus.SUCCESS -> {
                    hideDialog()
                    setData(resources.data)
                }
                ApiStatus.ERROR -> {
                    hideDialog()
                    resources.message?.let {
                        constraintCity.snackBar(it)
                    }
                }
            }
        })

        cityViewModel.fiveDaysForecastLiveData.observe(viewLifecycleOwner, { resources ->
            when (resources.status) {
                ApiStatus.LOADING -> {
                    // do nothing
                }
                ApiStatus.SUCCESS -> {
                    setFiveDaysForecastData(resources.data)
                }
                ApiStatus.ERROR -> {
                    resources.message?.let {
                        constraintCity.snackBar(it)
                    }
                }
            }
        })
    }

    private fun setFiveDaysForecastData(data: FiveDaysForecastResponse?) {
        data?.let {
            adapter.updateData(it.list)
        }
    }

    private fun setData(data: ForecastResponse?) {
        data?.let {
            cardForecast.show()

            tvWind.setTextOrHide(
                getString(
                    R.string.wind_info,
                    String.format("%.2f", it.wind.speed)
                )
            )
            with(it.main) {
                tvTemperature.setTextOrHide(getTemperature(temp))
                tvHumidity.setTextOrHide(getString(R.string.humidity_info, humidity))
            }

            with(it.weather.first()) {
                tvWeather.setTextOrHide(description)
            }
        }
    }

    private fun getTemperature(minTemp: Float): String? {
        return getString(
            R.string.temperature_in_centi,
            String.format("%.2f", minTemp - 273.1)
        )
    }
}