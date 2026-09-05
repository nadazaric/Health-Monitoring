package com.healthmonitoring.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.feature.heart_rate.presentation.component.HeartRateCard
import com.healthmonitoring.mobile.feature.ppg.presentation.component.PpgCard
import com.healthmonitoring.mobile.feature.skin_temperature.presentation.component.SkinTemperatureCard
import com.healthmonitoring.mobile.feature.spo2.presentation.component.SpO2Card
import com.healthmonitoring.mobile.ui.theme.Dimens
import com.healthmonitoring.mobile.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var healthDataReceiver: HealthDataReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthMonitoringTheme {
                MobileApp()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        healthDataReceiver.startListening()
    }

    override fun onPause() {
        healthDataReceiver.stopListening()

        super.onPause()
    }
}

@Composable
fun MobileApp() {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                vertical = Dimens.ScreenVerticalPadding,
                horizontal = Dimens.ScreenHorizontalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.CardSpacing),
            horizontalArrangement = Arrangement.spacedBy(Dimens.CardSpacing)
        ) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}