package com.mobiquity.assesment.service.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    val latitude: Double,
    val longitude: Double,
    val cityName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}