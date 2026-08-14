package com.healthmonitoring.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import com.healthmonitoring.wear.core.presentation.MonitoringHost
import com.healthmonitoring.wear.feature.heart_rate.presentation.component.HeartRateCard
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel
import com.healthmonitoring.wear.feature.ppg.presentation.component.PpgCard
import com.healthmonitoring.wear.feature.ppg.presentation.component.PpgMeasurementScreen
import com.healthmonitoring.wear.feature.skin_temperature.presentation.component.SkinTemperatureCard
import com.healthmonitoring.wear.feature.spo2.presentation.component.SpO2Card
import com.healthmonitoring.wear.ui.theme.Dimens
import com.healthmonitoring.wear.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val ppgViewModel: PpgViewModel = hiltViewModel()

    var isPpgScreenVisible by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(
        enabled = isPpgScreenVisible
    ) {
        if (ppgViewModel.state.value.isMeasuring) {
            ppgViewModel.stopMeasurement()
        }

        isPpgScreenVisible = false
    }

    HealthMonitoringTheme {
        MonitoringHost {
            AppScaffold {
                if (isPpgScreenVisible) {
                    PpgMeasurementScreen(
                        viewModel = ppgViewModel
                    )
                } else {
                    HealthMetricsScreen(
                        ppgViewModel = ppgViewModel,
                        onPpgClick = {
                            isPpgScreenVisible = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthMetricsScreen(
    ppgViewModel: PpgViewModel,
    onPpgClick: () -> Unit
) {
    val listState = rememberTransformingLazyColumnState()

    TransformingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(
            horizontal = Dimens.ScreenHorizontalPadding,
            vertical = Dimens.ScreenVerticalPadding
        ),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.CardSpacing
        )
    ) {
        item {
            HeartRateCard(
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SkinTemperatureCard(
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SpO2Card(
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PpgCard(
                onClick = onPpgClick,
                modifier = Modifier.fillMaxWidth(),
                viewModel = ppgViewModel
            )
        }
    }
}