package com.healthmonitoring.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.feature.heartrate.presentation.component.HeartRateCard
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
    LazyVerticalGrid(
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
                text = "Health Monitoring",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        item {
            HeartRateCard(
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}