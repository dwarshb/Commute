package com.dwarshb.commute.utils

import com.google.android.gms.maps.model.LatLng

/**
 * It is contains the constants value used across app.
 *
 * Created at : December 24, 2021
 */
object Constants {
    val DEFAULT_LOCATION_ORIGIN: LatLng = LatLng(53.694865,9.757589)
    val DEFAULT_LOCATION_DESTINATION: LatLng = LatLng(53.394655,10.099891)
    val BASE_URL = "https://fake-poi-api.mytaxi.com"
    object Intent {
        val BODY: String = "body"
        val VEHICLE = "vehicle"
    }
}