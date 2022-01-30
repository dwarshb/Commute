package com.dwarshb.freenowsample.presentation.splashScreen

import androidx.lifecycle.ViewModel
import com.dwarshb.freenowsample.domain.FetchVehicles
import com.dwarshb.freenowsample.presentation.State
import com.dwarshb.freenowsample.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * SplashViewModel is used to get the trigger the fetchRequest from the API.
 *
 * Created at December 24, 2021
 *
 * @author Darshan Bhanushali
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val fetchRequest: FetchVehicles) :
    ViewModel() {

    fun getResponse() = flow {
        emit(State.LoadingState)
        try {
            delay(1000)
            emit(State.DataState(fetchRequest()))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Utils.resolveError(e))
        }
    }
}