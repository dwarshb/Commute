package com.dwarshb.commute.presentation.mapScreen

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dwarshb.commute.R
import com.dwarshb.commute.data.Vehicle
import com.dwarshb.commute.databinding.ActivityMapsBinding
import com.dwarshb.commute.utils.Constants
import com.dwarshb.commute.utils.Utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil

/**
 * MapsActivity is used to display a various vehicles from the list to map. It shows vehicle icon
 * at the designated position and display its information. Whenever user select any marker from the
 * map, then it will redirect the camera of map to it's location. Also on click of dialog shown at
 * the top, google will be opened with the route of those direction which includes origin & destination
 * as well.
 *
 * Created at : December 26, 2021
 *
 * @see MapsViewModel
 * @see Utils
 *
 * @author Darshan Bhanushali
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var vehicleList = mutableListOf<Vehicle>()

    private lateinit var mapsViewModel : MapsViewModel

    private var markerFromIntent : MarkerOptions? = null

    /**
     * It is default method which is executed after the activity is loaded
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide car display info at the beginning
        binding.vehicleItem.carItem.visibility = View.GONE

        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        //Handle Intent to get list of vehicles
        if (intent.hasExtra(Constants.Intent.BODY)) {
            vehicleList = intent
                .getParcelableArrayListExtra<Vehicle>(Constants.Intent.BODY)?.toMutableList()!!
        }

        //Handle Intent to display vehicle details if available
        if (intent.hasExtra(Constants.Intent.VEHICLE)) {
            markerFromIntent = mapsViewModel.getVehicleMarkerFromIntent(baseContext, intent)
        }

        mapsViewModel.setVehicleList(vehicleList)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Hide progressindicator once map is loaded successfully
        binding.progressIndicator.visibility = View.GONE

        mMap.setOnMarkerClickListener(this)

        //Push the list of vehicle in map.
        mapsViewModel.pushVehicleListInMap(baseContext,mMap)

        //Set Camera of Location to ORIGIN
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.DEFAULT_LOCATION_ORIGIN))
        mMap.setMinZoomPreference(10f)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        //Set UISettings for Map
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        //Get the center from origin & destination and cover the area using CircleOptions
        val center = mapsViewModel.getCenter()
        val distance = SphericalUtil.computeDistanceBetween(
            Constants.DEFAULT_LOCATION_ORIGIN,Constants.DEFAULT_LOCATION_DESTINATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mMap.addCircle(CircleOptions()
                .center(center)
                .fillColor(getColor(R.color.transparent_primary))
                .strokeColor(getColor(R.color.transparent_primary))
                .strokeWidth(2f)
                .radius(distance/2))
        }

        //Used to add the marker for a vehicle received from an Intent and move camera to it.
        if (markerFromIntent!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerFromIntent?.position))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
            val marker = mMap.addMarker(markerFromIntent)
            displayCarInfo(marker)
        }
    }

    /**
     * Used to perform operation on click of any marker in map.
     * i.e Move Camera of map to center bound of the marker and display vehicle information using
     * <code>displayCarInfo(marker : Marker)</code>
     *
     * @param marker is a object of Marker shown in map.
     *
     * @see Marker
     * @see displayCarInfo
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        mapsViewModel.centerBound(marker)
        displayCarInfo(marker)
        return true
    }

    /**
     * It is used to display the vehicle information from the data available in marker.
     * Ex: title,position,heading
     *
     * @param marker is a object of Marker shown in Map
     *
     * @author Darshan Bhanushali
     */
    private fun displayCarInfo(marker: Marker?) {
        //If carItem isn't visible perform animation
        if (binding.vehicleItem.carItem.visibility==View.GONE) {
            val animator = ObjectAnimator.ofFloat(
                binding.vehicleItem.carItem, View.TRANSLATION_Y,
                -360f, 0f
            )
            animator.duration = 1000
            animator.start()
        }
        binding.vehicleItem.carItem.visibility = View.VISIBLE

        //Handle vehicle data from marker and show it in UI
        val fleetType = marker?.title
        val address = marker?.snippet
        val heading = marker?.rotation
        binding.vehicleItem.fleetType.text = fleetType
        binding.vehicleItem.carAddress.text = address
        binding.vehicleItem.directionArrow.rotation = heading!!
        binding.vehicleItem.directionText.text =
            Utils.getDirectionFromHeading(baseContext, heading.toDouble())
        if(fleetType.equals("Pooling",ignoreCase = true)){
            binding.vehicleItem.carImage.setImageResource(R.drawable.car2)
        } else{
            binding.vehicleItem.carImage.setImageResource(R.drawable.taxi)
        }

        //perform operation on click of carItem Dialog at the top.
        binding.vehicleItem.carItem.setOnClickListener {
            val uri : String = mapsViewModel.getUriFromMarker(marker)
            val mapIntent = Intent(Intent.ACTION_VIEW)
            mapIntent.data = Uri.parse(uri)
            startActivity(mapIntent)

        }
    }
}