package com.mobiquity.assesment.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mobiquity.assesment.R
import com.mobiquity.assesment.base.BaseFragment
import com.mobiquity.assesment.service.database.entity.City
import com.mobiquity.assesment.ui.city.CityFragment.Companion.CITY_ID_ARG
import com.mobiquity.assesment.utils.hide
import com.mobiquity.assesment.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import androidx.recyclerview.widget.GridLayoutManager





@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var cityAdapter: CityAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun getLayoutResourceId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initListener()
        initViewModel()
    }

    private fun setAdapter() {
        cityAdapter = CityAdapter(::cityCallback)
        rvCities.adapter = cityAdapter
    }

    private fun cityCallback(shouldDelete: Boolean, city: City) {
        if (shouldDelete) {
            viewModel.deleteCityById(city.id)
        } else {
            findNavController().navigate(
                R.id.actionHomeToCityDest,
                bundleOf(CITY_ID_ARG to city.id)
            )
        }
    }

    private fun initListener() {
        fabAddLocation.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToSelectCityDest)
        }

        ivNoCity.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToSelectCityDest)
        }

        fabHelp.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToHelp)
        }
    }

    private fun initViewModel() {
        viewModel.getAllCities().observe(viewLifecycleOwner, { cities ->
            cities.takeIf { it.size > 0 }?.let {
                cityAdapter.updateData(it)
                rvCities.show()
                groupNoLocation.hide()
            } ?: run {
                rvCities.hide()
                groupNoLocation.show()
            }
        })
    }
}