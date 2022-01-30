package com.dwarshb.freenowsample.presentation.splashScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dwarshb.freenowsample.R
import com.dwarshb.freenowsample.presentation.mainScreen.MainActivity
import com.dwarshb.freenowsample.presentation.State
import com.dwarshb.freenowsample.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * SplashActivity is Entry Point of an application. It display loading animation on app icon till
 * the data gets fetched from API successfully
 **
 * Created on December 24, 2021
 *
 * @author Darshan Bhanushali
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val progressBar = findViewById<ProgressBar>(R.id.loading_circular)
        val icon = findViewById<ImageView>(R.id.icon)
        val title = findViewById<TextView>(R.id.title)
        lifecycleScope.launch {
            delay(500)
            viewModel.getResponse().collect {
                when(it) {
                    is State.DataState -> {
                        //On Success, scale the image and pass the fetched data to another activity
                        icon.animate().scaleXBy(0.1f).setDuration(500).start()
                        icon.animate().scaleYBy(0.1f).setDuration(500).start()
                        title.visibility = View.VISIBLE
                        title.animate().alpha(0f).alphaBy(1.0f).setDuration(700).start()
                        delay(2500)
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        intent.putExtra(Constants.Intent.BODY,
                            it.data.getAsJsonArray("poiList").toString())
                        startActivity(intent)
                        finish()
                    }
                    is State.ErrorState -> {
                        //On Error, display Toast.
                        progressBar.isIndeterminate = false
                        Toast.makeText(this@SplashActivity,
                            it.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                    is State.LoadingState -> {
                        progressBar.isIndeterminate = true
                    }
                }
            }
        }
    }
}
