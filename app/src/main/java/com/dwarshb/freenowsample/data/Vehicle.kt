package com.dwarshb.freenowsample.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

/**
 * It is data class which is used to handle the each item of vehicle list.
 *
 * @author Darshan Bhanushali
 *
 * Created on December 24,2021
 */
@Parcelize
data class Vehicle(
    var id:String?= null,
    var coordinate:LatLng?=null,
    var fleetType:String?=null,
    var heading: String?=null
) : Parcelable