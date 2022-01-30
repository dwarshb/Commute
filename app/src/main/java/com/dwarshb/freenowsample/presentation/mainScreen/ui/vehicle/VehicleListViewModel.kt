package com.dwarshb.freenowsample.presentation.mainScreen.ui.vehicle

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dwarshb.freenowsample.data.Vehicle
import com.dwarshb.freenowsample.utils.Utils

/**
 * VehicleListViewModel is used to handle the operation which are used to manage VehicleListFragment.
 *
 * Created at: December 25, 2021
 *
 * @author Darshan Bhanushali
 *
 */
class VehicleListViewModel : ViewModel() {

    val vehicleList: MutableLiveData<MutableList<Vehicle>> = MutableLiveData()
    val filteredVehicleList: MutableLiveData<MutableList<Vehicle>> = MutableLiveData()

    /**
     * This method is used to the data to vehicleList parameter
     *
     * @param dataBody : It is string object which has a data in JSONFormat
     *
     * @author Darshan Bhanushali
     */
    fun setVehicleList(dataBody : String) {
        dataBody?.let {
            vehicleList.value = Utils.parseJSONToList(dataBody)
        }
    }

    /**
     * This method is used to filter the vehicle List based on <b>Fleet Type & Heading Direction</b>
     *  @param context is a Context object used to get string from resources.
     *  @param fleetTypeStr is a String Object and parameter of Vehicle class
     *  @param headingDirection is a String Object and parameter of Vehicle class
     *
     *  @see Vehicle
     *  @see Utils.getDirectionFromHeading
     *
     *  @author Darshan Bhanushali
     */
    fun filterList(context : Context, fleetTypeStr: String?, headingDirection: String?) {
        if (fleetTypeStr.equals("All",ignoreCase = true)) {
            if (!headingDirection.equals("All")) {
                val filterList =
                    vehicleList.value?.filter {
                                Utils.getDirectionFromHeading(context, it.heading?.toDouble()!!)
                                    ?.equals(headingDirection)!!
                    }
                filteredVehicleList.value = filterList?.toMutableList()
                return
            }
            filteredVehicleList.value = vehicleList.value
        } else {
            if (headingDirection.equals("All",ignoreCase = true)) {
                val filterList =
                    vehicleList.value?.filter {
                        it.fleetType.equals(fleetTypeStr, ignoreCase = true)
                    }
                filteredVehicleList.value = filterList?.toMutableList()
            } else {
                val filterList =
                    vehicleList.value?.filter {
                        it.fleetType.equals(fleetTypeStr, ignoreCase = true) &&
                                Utils.getDirectionFromHeading(context, it.heading?.toDouble()!!)
                                    ?.equals(headingDirection)!!
                    }
                filteredVehicleList.value = filterList?.toMutableList()
            }
        }
    }

}