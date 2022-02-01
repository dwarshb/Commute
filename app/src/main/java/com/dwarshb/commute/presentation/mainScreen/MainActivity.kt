package com.dwarshb.commute.presentation.mainScreen

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dwarshb.commute.R
import com.dwarshb.commute.databinding.ActivityMainBinding
import com.dwarshb.commute.utils.Constants

/**
 * It is Main Screen which includes BottomNavigationBar to navigate between two fragments
 * i.e VehicleFragment & DashboardFragment
 *
 * Created at : December 24, 2021
 *
 * @see com.dwarshb.commute.presentation.mainScreen.ui.vehicle.VehicleListFragment
 * @see com.dwarshb.commute.presentation.mainScreen.ui.dashboard.DashboardFragment
 *
 * @author Darshan Bhanushali
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var bodyResponse : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bodyResponse = intent.getStringExtra(Constants.Intent.BODY).toString()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_vehicle, R.id.navigation_dashboard
            )
        )

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        val navArgument=NavArgument.Builder().setDefaultValue(bodyResponse).build()
        graph.addArgument(Constants.Intent.BODY,navArgument)

        navController.graph=graph
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            run {
                if (destination.id == R.id.navigation_vehicle) {
                    val args = NavArgument.Builder().setDefaultValue(bodyResponse).build()
                    destination.addArgument(Constants.Intent.BODY, args)
                }
            }
        }
    }
}