package com.dwarshb.freenowsample.domain


import com.dwarshb.freenowsample.data.API
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * This method is used to execute the API. It passes the require data and returns the response
 *
 * @author Darshan Bhanushali
 *
 * Created on December 24, 2021
 */
class FetchVehicles @Inject constructor(
    private val apIs: API
) {
    suspend operator fun invoke(): JsonObject {
        val response = apIs.getSampleVehicles(53.694865,9.757589,53.394655,10.099891)
        //here you can add some domain logic or call another UseCase
        return response
    }
}