package com.mobiquity.assesment.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mobiquity.assesment.R
import com.mobiquity.assesment.ui.home.SelectCityFragment.Companion.LOCATION_REQUEST_CODE
import java.text.SimpleDateFormat
import java.util.*

fun View.snackBar(text: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(this, text, duration).apply { show() }
}

fun TextView.setTextOrHide(text: String?) {
    text?.let {
        setText(it)
        show()
    } ?: hide()
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Context.hasLocationPermission() = ActivityCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
) == PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requestLocationPermission() {
    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    requestPermissions(permissions, LOCATION_REQUEST_CODE)
}

fun Long.getFormattedTime(format: String = "hh:mm aa"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun ImageView.setImage(imageUrl: String?) {
    show()
    Glide.with(this)
        .load(imageUrl)
        .placeholder(R.drawable.ic_weather)
        .dontAnimate()
        .into(this)
}
