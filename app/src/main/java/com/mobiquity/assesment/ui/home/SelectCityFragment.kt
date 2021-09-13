package com.mobiquity.assesment.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobiquity.assesment.R
import com.mobiquity.assesment.base.BaseFragment
import com.mobiquity.assesment.service.database.entity.City
import com.mobiquity.assesment.utils.hasLocationPermission
import com.mobiquity.assesment.utils.requestLocationPermission
import com.mobiquity.assesment.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_city.*
import java.io.IOException
import java.util.*
import com.google.android.gms.maps.GoogleMap.OnMapClickListener





@AndroidEntryPoint
class SelectCityFragment : BaseFragment(), OnMapReadyCallback {

    companion object {
        const val LOCATION_REQUEST_CODE = 1001
        const val CURRENT_LOCATION_REFRESH_INTERVAL: Long = 180000 /* 180 secs */
        const val CURRENT_LOCATION_FASTEST_INTERVAL: Long = 180000 /* 180 secs */
    }

    private lateinit var city: City
    private lateinit var mMap: GoogleMap
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun getLayoutResourceId() = R.layout.fragment_select_city

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        btnOkay.setOnClickListener {
            if (::city.isInitialized) {
                viewModel.addCity(city)
                findNavController().popBackStack()
            } else {
                constraintSelectCity.snackBar(getString(R.string.unable_to_fetch_location))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkIfGPSIsEnabledOrNot()
    }

    override fun onResume() {
        super.onResume()
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun checkLocationPermission() {
        if (requireContext().hasLocationPermission()) {
            getLocationUpdate()
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requireActivity().requestLocationPermission()
            }
        }
    }

    private fun checkIfGPSIsEnabledOrNot() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest.create().apply {
            interval = CURRENT_LOCATION_REFRESH_INTERVAL
            fastestInterval = CURRENT_LOCATION_FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // Check whether location settings are satisfied
        LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build())
            .addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)
                    // All location settings are satisfied.
                    checkLocationPermission()
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                // Cast to a resolvable exception.
                                val resolvable: ResolvableApiException =
                                    exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    requireActivity(), LocationRequest.PRIORITY_HIGH_ACCURACY
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            constraintSelectCity.snackBar(getString(R.string.unable_to_change_location_service))
                        }
                    }
                }
            }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult?.let {
                with(it.lastLocation) {
                    LatLng(latitude, longitude)
                }
            } ?: run {
                LatLng(17.387140, 78.491684) // if we don't get location
            }
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .draggable(true)
                    .title(getString(R.string.my_location))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))

            getGeoCodedLocation(location)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    getLocationUpdate()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationUpdate() {
        if (!requireContext().hasLocationPermission()) return
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, getMainLooper()
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(12.0f)
            uiSettings.isMapToolbarEnabled = false
            uiSettings.isZoomControlsEnabled = false
        }

        mMap.setOnMapClickListener { latlng -> // TODO Auto-generated method stub
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .draggable(true)
                    .title(getString(R.string.my_location))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12f))
            getGeoCodedLocation(latlng)

        }
    }

    private fun getGeoCodedLocation(latLng: LatLng) {
        if(latLng != null){
            val geoCoder = Geocoder(context, Locale.getDefault())
            try {
                val address =
                    geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1).first()
                if(address.locality != null){
                    city = City(latLng.latitude, latLng.longitude, address.locality)
                    constraintSelectCity.snackBar(address.locality)
                        .show()
                }else{
                    constraintSelectCity.snackBar(getString(R.string.something_went_wrong))
                        .show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }else {
            constraintSelectCity.snackBar(getString(R.string.something_went_wrong))
                .show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationRequest.PRIORITY_HIGH_ACCURACY -> {
                if (resultCode == Activity.RESULT_OK) {
                    checkLocationPermission()
                } else {
                    constraintSelectCity.snackBar(getString(R.string.device_location_enabled_declined))
                }
            }
        }
    }
}