package com.dwarshb.freenowsample.presentation.mainScreen.ui.vehicle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dwarshb.freenowsample.R
import com.dwarshb.freenowsample.data.Vehicle
import com.dwarshb.freenowsample.databinding.VehicleItemBinding
import com.dwarshb.freenowsample.presentation.mapScreen.MapsActivity
import com.dwarshb.freenowsample.utils.Constants
import com.dwarshb.freenowsample.utils.Utils

/**
 * VehicleListAdapter is a Adapter class which is used to manage the list of vehicle. It is used
 * to manage each item of the vehicle list and perform required operations such as OnItemClick.
 *
 * Created at : December 25, 2021
 *
 * @see VehicleListFragment
 * @see MapsActivity
 *
 * @author Darshan Bhanushali
 */
class VehicleListAdapter(val context: Context) :
    RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {

    //Create a Mutable List to store list of vehicles
    private val vehiclesList: MutableList<Vehicle> = mutableListOf()

    /**
     * It is used to set the vehicle list.
     * @param vehicles is a MutableList which includes list of vehciles
     *
     *  @author Darshan Bhanushali
     */
    fun setList(vehicles : MutableList<Vehicle>) {
        vehiclesList.addAll(vehicles)
        notifyDataSetChanged()
    }

    /**
     * It is used to clear/reset the vehicle list of adapter.
     *
     *  @author Darshan Bhanushali
     */
    fun clear() {
        vehiclesList.clear()
        notifyDataSetChanged()
    }

    /**
     * Initial part of Adapter class whenever view is created
     * @return VehicleViewHolder
     *
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VehicleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        val binding  = VehicleItemBinding.inflate(layoutInflater)

        return VehicleViewHolder(binding)
    }

    /**
     * Returns count of items in vehicle list.
     * @return Int
     *
     */
    override fun getItemCount(): Int {
        return vehiclesList.size
    }

    /**
     * It is used to manage the data in each item of vehicle list.
     * @param vehicleViewHolder is object of VehicleViewHolder, used to bind UI with a data.
     * @param position is a integer which includes position of vehicle from the list.
     *
     */
    @SuppressLint("NewApi")
    override fun onBindViewHolder(vehicleViewHolder: VehicleViewHolder, position: Int) {

        //get vehicle from the list using a position
        val vehicle =vehiclesList.toMutableList()[position]

        vehicleViewHolder.binding.fleetType.text = vehicle.fleetType

        //compare the fleet type and update UI as per it.
        if (vehicle.fleetType.equals("POOLING",ignoreCase = true)) {
            vehicleViewHolder.binding.carImage.setImageResource(R.drawable.car2)
            vehicleViewHolder.binding.fleetType.setBackgroundColor(context.getColor(R.color.blue))
            vehicleViewHolder.binding.fleetType.setTextColor(context.getColor(R.color.white))
        } else {
            vehicleViewHolder.binding.carImage.setImageResource(R.drawable.taxi)
            vehicleViewHolder.binding.fleetType.setBackgroundColor(context.getColor(R.color.yellow))
            vehicleViewHolder.binding.fleetType.setTextColor(context.getColor(R.color.black))
        }

        val heading = vehicle.heading?.toDouble()!!

        val directionString = Utils.getDirectionFromHeading(context,heading)
        when {
            directionString?.contains("North East")!! -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_north_east_24dp)
            directionString.contains("North West") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_north_west_24dp)
            directionString.contains("South East") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_south_east_24dp)
            directionString.contains("South West") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_south_west_24dp)
            directionString.contains("South") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_south_24dp)
            directionString.contains("North") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_north_24dp)
            directionString.contains("East") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_east_24dp)
            directionString.contains("West") -> vehicleViewHolder.binding.directionArrow.setImageResource(R.drawable.ic_west_24dp)
        }

        //animate arrow
        vehicleViewHolder.binding.directionArrow.animate().rotationBy(360f).setDuration(500).start()

        /*
        * Use Utils.getDirectionFromHeading() to get the direction in readable format
        *  Use Utils.getAddressFromCoordinates() to get address in readable format
        */
        vehicleViewHolder.binding.directionText.text = Utils.getDirectionFromHeading(context,heading)
        vehicleViewHolder.binding.carAddress.text = Utils.getAddressFromCoordinates(context,vehicle.coordinate!!)

        /*
        * On click of particular Vehicle. Remove it from and list first and then pass it to
        * MapActivity along with list of vehicles. So it will be handled under MapsActivity
        */
        vehicleViewHolder.binding.carItem.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val listToPass = ArrayList<Vehicle>(vehiclesList)
                listToPass.remove(vehicle)
                val intent = Intent(context,MapsActivity::class.java)
                intent.putExtra(Constants.Intent.VEHICLE,vehicle)
                intent.putParcelableArrayListExtra(Constants.Intent.BODY,listToPass)
                context.startActivity(intent)
            }

        })

    }

    /**
     * It is Data class which is useful to bind UI for each vehicle item
     */
    data class VehicleViewHolder(val binding: VehicleItemBinding) : RecyclerView.ViewHolder(binding.root)
}