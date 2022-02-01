package com.dwarshb.commute

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.runners.MethodSorters
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import androidx.test.uiautomator.UiSelector

import androidx.test.uiautomator.UiObject




/**
 * MapsActivityTest includes multiple testcases which can be executed for MapsActivity
 * specifically
 *
 * @author Darshan Bhanushali
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MapsActivityTest {
    val PACKAGE : String = "com.dwarshb.freenowsample"
    val TIMEOUT : Int = 5000
    var uiDevice : UiDevice? = null
    var bottomNavigation : BottomNavigationView? = null

    @Before
    fun setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager
            .getLaunchIntentForPackage(PACKAGE)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        // Clear out any previous instances
        context.startActivity(intent)
        uiDevice?.wait<Boolean>(Until.hasObject(By.pkg(PACKAGE).depth(0)),
            TIMEOUT.toLong())
        Thread.sleep(8000)
        Espresso.onView(withId(R.id.maps_fab)).perform(ViewActions.click())
    }

    /**
     * This test assures that data get loaded in map view and perform certain actions like
     * click on marker action. i.e Taxi
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun A_clickOnTaxiMarker() {
        try {
            val marker: UiObject =
                uiDevice?.findObject(UiSelector().descriptionContains("Taxi"))!!
            marker.click()
            Thread.sleep(4000)
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * This test assures that data get loaded in map view and perform certain actions like
     * click on marker action. i.e Pooling
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun B_clickOnPoolingMarker() {
        try {
            val marker: UiObject =
                uiDevice?.findObject(UiSelector().descriptionContains("Pooling"))!!
            marker.click()
            Thread.sleep(4000)
        } catch (e : Exception) {
            Assert.fail(e.message)
        }
    }
}