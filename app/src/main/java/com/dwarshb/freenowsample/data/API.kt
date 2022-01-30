package com.dwarshb.freenowsample.data

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API class is used to query the api used within App.
 *
 * @author Darshan Bhanushali
 *
 * Created on December 24, 2021
 */
interface API {
    @GET("/")
    suspend fun getSampleVehicles(@Query("p1Lat") lat1: Double,
                                  @Query("p1Lon") long1: Double,
                                  @Query("p2Lat") lat2: Double,
                                  @Query("p2Lon") long2: Double): JsonObject
}