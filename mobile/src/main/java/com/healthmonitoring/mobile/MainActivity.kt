package com.healthmonitoring.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var healthDataReceiver: HealthDataReceiver

    private val heartRateText = mutableStateOf("-- BPM")
    private val connectionText = mutableStateOf("Waiting for watch data...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthMonitoringTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Health Monitoring",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Heart rate: ${heartRateText.value}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        healthDataReceiver.startListening(
            onHeartRateReceived = { measurement ->
                runOnUiThread {
                    heartRateText.value = "${measurement.bpm} BPM"
                }
            },
            onNoHeartRateDataFound = {
                runOnUiThread {
                    connectionText.value = "Waiting for watch data..."
                }
            }
        )
    }

    override fun onPause() {
        healthDataReceiver.stopListening()

        super.onPause()
    }
}