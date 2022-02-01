package com.dwarshb.commute.utils

import android.content.Context
import android.location.Geocoder
import com.dwarshb.commute.R
import com.dwarshb.commute.data.Vehicle
import com.dwarshb.commute.presentation.AuthenticationException
import com.dwarshb.commute.presentation.NetworkErrorException
import com.dwarshb.commute.presentation.State
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

/**
 * Utils class is used to handle the independent operations. It is used across app.
 *
 * Created at : December 25, 2021
 *
 * @author Darshan Bhanushali
 */
class Utils {
    companion object{

        /**
         * It is used to parse the JSONOject to MutableList<Vehicle> using Gson
         *
         * @param jsonObject is a String object which includes data in form of JSONObject
         *
         * @return MutableList<Vehicle>
         *
         * @author Darshan Bhanushali
         */
        fun parseJSONToList(jsonObject: String): MutableList<Vehicle> {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Vehicle>>() {}.type
            return gson.fromJson(jsonObject, type)
        }

        /**
         * It is used to get address in readable format from the coordinates passed in parameters.
         * It uses GeoCoder API to get address from latitude & longitude and return the same address
         *
         * @param context is object of Context
         * @param coordinate is object of LatLng
         *
         * @return CharSequence
         *
         * @author Darshan Bhanushali
         */
        fun getAddressFromCoordinates(context:Context, coordinate: LatLng): CharSequence? {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
                if (addresses != null && addresses.size > 0) {
                    return addresses[0].getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * It is used to handle the Network errors and return State based on error.
         *
         * @param e is object of Exception
         *
         * @return State.ErrorState
         *
         * @author Darshan Bhanushali
         */
        fun resolveError(e: Exception): State.ErrorState {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "connection error!")
                }
                is ConnectException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
            }

            if(e is HttpException){
                when(e.code()){
                    502 -> {
                        error = NetworkErrorException(e.code(),  "internal error!")
                    }
                    401 -> {
                        throw AuthenticationException("authentication error!")
                    }
                    400 -> {
                        error = NetworkErrorException.parseException(e)
                    }
                }
            }


            return State.ErrorState(error)
        }

        /**
         * It is used to manage the direction from the heading passed in parameter.
         *
         * @param context is object of Context
         * @param heading is object of Heading which is Double in format.
         *
         * @return CharSequence
         *
         * @author Darshan Bhanushali
         */
        fun getDirectionFromHeading(context: Context,heading: Double): CharSequence? {
            if (heading<315 && heading>=270) {
                return context.getString(R.string.north_west)
            } else if (heading<270 && heading>=225) {
                return context.getString(R.string.west)
            } else if (heading<225 && heading>=180) {
                return context.getString(R.string.south_west)
            } else if (heading<180 && heading>=135) {
                return context.getString(R.string.south)
            } else if (heading<135 && heading>=90) {
                return context.getString(R.string.south_east)
            } else if (heading<90 && heading>=45) {
                return context.getString(R.string.north_east)
            } else {
                return context.getString(R.string.north)
            }
        }

    }
}