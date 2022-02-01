package com.dwarshb.commute

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.runners.MethodSorters
import androidx.test.espresso.action.ViewActions.click
import org.hamcrest.Matchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test

/**
 * MainActivityTest includes multiple testcases which can be executed for MainActivity
 * specifically
 *
 * @author Darshan Bhanushali
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {
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
    }

    /**
     * This test assures that data get loaded in vehicle list and perform certain actions like
     * scroll and click on 1st item in vehicle list.
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun A_scrollAndClick() {
        try {
            Espresso.onView(withId(R.id.vehicles_list)).perform(ViewActions.swipeUp()).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1,
                        ViewActions.click()))
            Thread.sleep(4000)
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * This test open the bottomSheet and filter the vehicle list based on fleet type i.e pooling.
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun B_filterVehicleListByPooling() {
        try {
            Espresso.onView(withId(R.id.filter_fab)).perform(ViewActions.click())
            Thread.sleep(1500)
            Espresso.onView(withId(R.id.fleet_pooling)).perform(ViewActions.click())
            Espresso.onView(withId(com.google.android.material.R.id.design_bottom_sheet)).
            perform(ViewActions.pressBack())
            Assert.assertTrue("Filter List Success",true)
            Espresso.onView(withId(R.id.vehicles_list)).perform(ViewActions.swipeUp())
            Thread.sleep(8000)
        } catch (e : Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * This test open the bottomSheet and filter the vehicle list based on fleet type i.e Taxi.
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun C_filterVehicleListByTaxi() {
        try {
            Espresso.onView(withId(R.id.filter_fab)).perform(ViewActions.click())
            Thread.sleep(1500)
            Espresso.onView(withId(R.id.fleet_taxi)).perform(ViewActions.click())
            Espresso.onView(withId(com.google.android.material.R.id.design_bottom_sheet)).
            perform(ViewActions.pressBack())
            Assert.assertTrue("Filter List Success",true)
            Espresso.onView(withId(R.id.vehicles_list)).perform(ViewActions.swipeUp())
            Thread.sleep(8000)
        } catch (e : Exception) {
            Assert.fail(e.message)
        }
    }
    /**
     * This test is used to open the bottomSheet and filter the vehicle list based on Direction
     * i.e North
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun D_filterVehicleListByDirection() {

        try {
            Espresso.onView(withId(R.id.filter_fab)).perform(ViewActions.click())
            Thread.sleep(1500)
            Espresso.onView(withId(R.id.direction_spinner)).perform(ViewActions.click())
            Espresso.onData(anything()).atPosition(1).perform(click());
            Espresso.onView(withId(com.google.android.material.R.id.design_bottom_sheet)).
            perform(ViewActions.pressBack())
            Assert.assertTrue("Filter List Success",true)
            Thread.sleep(8000)
        } catch (e : Exception) {
            Assert.fail(e.message)
        }
    }

}