package com.master.healthmonitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Text
import com.master.healthmonitoring.core.HealthTrackerProvider
import com.master.healthmonitoring.feature.heartrate.presentation.component.HeartRateCard
import com.master.healthmonitoring.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var healthTrackerProvider: HealthTrackerProvider

    private var isHealthTrackingConnected by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        healthTrackerProvider.setOnConnectedCallback {
            runOnUiThread {
                isHealthTrackingConnected = true
            }
        }

        healthTrackerProvider.connect()

        setContent {
            WearApp(
                isHealthTrackingConnected = isHealthTrackingConnected
            )
        }
    }

    override fun onDestroy() {
        healthTrackerProvider.disconnect()
        super.onDestroy()
    }
}

@Composable
fun WearApp(
    isHealthTrackingConnected: Boolean
) {
    HealthMonitoringTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()

            TransformingLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 40.dp),
                state = listState
            ) {
                item {
                    if (isHealthTrackingConnected) {
                        HeartRateCard(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            text = "Connecting to sensors..."
                        )
                    }
                }
            }
        }
    }
}