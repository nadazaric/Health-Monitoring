package com.master.healthmonitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.master.healthmonitoring.core.HealthTrackerProvider
import com.master.healthmonitoring.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var healthTrackerProvider: HealthTrackerProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        healthTrackerProvider.connect()

        setContent {
            WearApp("Android")
        }
    }

    override fun onDestroy() {
        healthTrackerProvider.disconnect()
        super.onDestroy()
    }
}

@Composable
fun WearApp(greetingName: String) {
    HealthMonitoringTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            val transformationSpec = rememberTransformationSpec()

            TransformingLazyColumn(state = listState) {
                item {
                    ListHeader(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec)
                                .padding(20.dp),
                        transformation = SurfaceTransformation(transformationSpec),
                    ) {
                        Text(text = stringResource(R.string.hello_world, greetingName))
                    }
                }
            }
        }
    }
}