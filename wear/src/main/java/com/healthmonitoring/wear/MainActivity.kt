package com.healthmonitoring.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import com.healthmonitoring.wear.core.presentation.MonitoringHost
import com.healthmonitoring.wear.feature.heartrate.presentation.component.HeartRateCard
import com.healthmonitoring.wear.feature.skin_temperature.presentation.component.SkinTemperatureCard
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
    HealthMonitoringTheme {
        MonitoringHost {
            AppScaffold {
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
                }
            }
        }
    }
}