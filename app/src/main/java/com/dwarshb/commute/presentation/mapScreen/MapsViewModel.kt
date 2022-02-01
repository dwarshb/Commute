package com.dwarshb.commute.presentation.mapScreen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dwarshb.commute.R
import com.dwarshb.commute.data.Vehicle
import com.dwarshb.commute.utils.Constants
import com.dwarshb.commute.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

/**
 * MapsViewModel is a class which is used to handle the operation for MapsActivity
 *
 * Created at December 26, 2021
 *
 * @see MapsActivity
 *
 * @author Darshan Bhanushali
 */
class MapsViewModel : ViewModel() {
    val vehicleList: MutableLiveData<MutableList<Vehicle>> = MutableLiveData()
    val filteredVehicleList: MutableLiveData<MutableList<Vehicle>> = MutableLiveData()

    private lateinit var mMap : GoogleMap

    /**
     * It is used to set the value in vehicleList
     * @param vehicleList is object MutableList<Vehicle>
     *
     * @author Darshan Bhanushali
     */
    fun setVehicleList(vehicleList : MutableList<Vehicle>) {
        this.vehicleList.value = vehicleList
    }

    /**
     * It is used to add marker in map for each vehicle in the list.
     *
     * @param context is a object of Context
     * @param mMap is a object of GoogleMap
     *
     * @see setVehicleMarker
     *
     * @author Darshan Bhanushali
     */
    fun pushVehicleListInMap(context: Context,mMap : GoogleMap) {
        this.mMap = mMap
        for (vehicle in vehicleList.value!!) {
            val marker = setVehicleMarker(context,vehicle)
            val markerOnMap = mMap.addMarker(marker)
            markerOnMap.tag = "VEHICLE"
            markerOnMap.rotation = vehicle.heading?.toFloat()!!
        }
    }

    /**
     * It is used to set the MarkerOptions from the vehicle data passed in the parameter and return
     * it once the data is set. Data includes, address of vehicle, fleet-type, position and
     * icon of marker. Also it sets different icons based on the fleet type.
     *
     * @param context is object of Context
     * @param vehicle is object of Vehicle
     *
     * @return MarkerOptions
     *
     * @see Utils.getAddressFromCoordinates
     * @see MarkerOptions
     *
     * @author Darshan Bhanushali
     */
    private fun setVehicleMarker(context: Context, vehicle: Vehicle) : MarkerOptions {
        //Get address in readable format from coodinates
        val address = Utils.getAddressFromCoordinates(context,vehicle.coordinate!!)
            .toString()

        //set icon based on the fleet type
        var marker = if (vehicle.fleetType.equals("Pooling",ignoreCase = true)) {
            BitmapDescriptorFactory.fromResource(R.drawable.car_marker)
        } else {
            BitmapDescriptorFactory.fromResource(R.drawable.taxi_marker)
        }

        //set the data in MarkerOptions and return it.
        return MarkerOptions().position(vehicle.coordinate!!)
            .title(vehicle.fleetType)
            .snippet(address)
            .icon(marker)

    }

    /**
     * It is used to get center bound of the vehicle position and animate camera of map to
     * marker location.
     *
     * @param marker
     *
     * @author Darshan Bhanushali
     */
    fun centerBound(marker: Marker?) {
        val builder = LatLngBounds.Builder()
        builder.include(marker?.position)
        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,200)
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.animateCamera(cameraUpdate,1500,
            object : GoogleMap.CancelableCallback{
                override fun onFinish() {
                    mMap.uiSettings.isScrollGesturesEnabled = true
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17f))
                }

                override fun onCancel() {
                    mMap.uiSettings.isScrollGesturesEnabled = true
                }
            })
    }

    /**
     * It is used to get center from Origin to Destination.
     * @return LatLng
     *
     * @see Constants
     *
     * @author Darshan Bhanushali
     */
    fun getCenter(): LatLng? {
        val latLngBounds = LatLngBounds.builder()
        latLngBounds.include(Constants.DEFAULT_LOCATION_ORIGIN)
        latLngBounds.include(Constants.DEFAULT_LOCATION_DESTINATION)
        return latLngBounds.build().center
    }

    /**
     * It is used to get the marker by extracting vehicle data from intent.
     * @param context is object of Context
     * @param intent is object of Intent used as getIntent to extract the data passed within it.
     *
     * @return MarkerOptions
     *
     * @see setVehicleMarker
     * @see MarkerOptions
     *
     * @author Darshan Bhanushali
     */
    fun getVehicleMarkerFromIntent(context: Context, intent: Intent): MarkerOptions {
        val vehicle = intent.getParcelableExtra<Vehicle>(Constants.Intent.VEHICLE)
        val marker = setVehicleMarker(context,vehicle!!)
        marker.rotation(vehicle.heading?.toFloat()!!)
        return marker
    }

    /**
     * It is used to open Google Map from the url and show the route from Origin to Destination
     * through waypoint i.e Location of marker passed in parameter
     *
     * @param marker is a object of Marker which includes location through which the route will be shown
     *
     * @return String
     *
     * @author Darshan Bhanushali
     */
    fun getUriFromMarker(marker: Marker) : String {
        val uri = "https://www.google.com/maps/dir/?api=1&" +
                "origin=${Constants.DEFAULT_LOCATION_ORIGIN.latitude}," +
                "${Constants.DEFAULT_LOCATION_ORIGIN.longitude}" +
                "&destination=${Constants.DEFAULT_LOCATION_DESTINATION.latitude}," +
                "${Constants.DEFAULT_LOCATION_DESTINATION.longitude}" +
                "&waypoints=${marker.position.latitude}," +
                "${marker.position.longitude}"
        return uri
    }
}