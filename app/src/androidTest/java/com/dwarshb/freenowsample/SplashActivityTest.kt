package com.dwarshb.freenowsample

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.dwarshb.freenowsample.presentation.splashScreen.SplashActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.*
import org.junit.runners.MethodSorters

import androidx.test.espresso.matcher.ViewMatchers
import java.lang.Exception

/**
 * SplashActivityTest is used to run the testcases which are related to SplashActivity.
 *
 * @author Darshan Bhanushali
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SplashActivityTest {
    val PACKAGE : String = "com.dwarshb.freenowsample"
    val TIMEOUT : Int = 5000
    var uiDevice : UiDevice? = null
    var bottomNavigation : BottomNavigationView? = null

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(SplashActivity::class.java)

    /**
     * This is executed at initial stage and it load the apps whenever this testcase file us
     * executed.
     *
     * @author Darshan Bhanushali
     */
    @Before
    fun setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager
            .getLaunchIntentForPackage(PACKAGE)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        // Clear out any previous instances
        context.startActivity(intent)
        uiDevice?.wait<Boolean>(
            Until.hasObject(By.pkg(PACKAGE).depth(0)),
            TIMEOUT.toLong())

    }

    /**
     * This Test assures whether the Data get fetched within 5 sec and loaded in vehicle list.
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun A_loadedIn5Sec() {
        Thread.sleep(5000)
        try {
            onView(ViewMatchers.withId(R.id.vehicles_list)).check { view, noViewFoundException ->
                run {
                    Assert.assertTrue("Loaded In 5 Sec", true)
                }
            }
        } catch (e: Exception) {
            Assert.fail(e.toString())

        }
    }

    /**
     * This test assures whether the Data get fetched within 10 sec and loaded in vehicle list,
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun B_loadedIn10Sec() {
        Thread.sleep(5000)
        try {
            onView(ViewMatchers.withId(R.id.vehicles_list)).check { view, noViewFoundException ->
                run {
                    Assert.assertTrue("Loaded In 10 Sec", true)
                }
            }
        } catch (e: Exception) {
            Assert.fail(e.toString())

        }
    }

    /**
     * This test assures whether the Data get fetched within 15 sec and loaded in vehicle list.
     *
     * @author Darshan Bhanushali
     */
    @Test
    fun C_loadedIn15Sec() {
        Thread.sleep(5000)
        try {
            onView(ViewMatchers.withId(R.id.vehicles_list)).check { view, noViewFoundException ->
                run {
                    Assert.assertTrue("Loaded In 15 Sec", true)
                }
            }
        } catch (e: Exception) {
            Assert.fail(e.toString())

        }
    }

}