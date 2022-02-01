package com.dwarshb.commute.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * Created at December 24, 2021
 *
 * @author Darshan Bhanushali
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}