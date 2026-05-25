package com.master.healthmonitoring

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.master.healthmonitoring.consts.Tags
import com.master.healthmonitoring.core.HealthTrackerProvider
import com.master.healthmonitoring.feature.heartrate.data.listener.HeartRateListener
import com.master.healthmonitoring.ui.theme.HealthMonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var healthTrackerProvider: HealthTrackerProvider

    @Inject
    lateinit var heartRateListener: HeartRateListener

    private val heartRatePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(Tags.MAIN_ACTIVITY, "Heart rate permission granted.")

                heartRateListener.startTracking()
            } else {
                Log.w(Tags.MAIN_ACTIVITY, "Heart rate permission denied.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start heart rate tracking only after the SDK connection is established.
        healthTrackerProvider.setOnConnectedCallback {
            startHeartRateTrackingWithPermission()
        }

        healthTrackerProvider.connect()

        setContent {
            WearApp("Android")
        }
    }

    private fun startHeartRateTrackingWithPermission() {
        val permission = getHeartRatePermission()

        val isGranted =
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            Log.d(Tags.MAIN_ACTIVITY, "Heart rate permission already granted.")

            heartRateListener.startTracking()
        } else {
            Log.d(Tags.MAIN_ACTIVITY, "Requesting heart rate permission: $permission")

            heartRatePermissionLauncher.launch(permission)
        }
    }

    private fun getHeartRatePermission(): String {
        return if (Build.VERSION.SDK_INT >= 36) {
            READ_HEART_RATE_PERMISSION
        } else {
            Manifest.permission.BODY_SENSORS
        }
    }

    override fun onDestroy() {
        // Stop sensor tracking before disconnecting from the SDK.
        heartRateListener.stopTracking()

        // Release the connection to Samsung Health Tracking Service.
        healthTrackerProvider.disconnect()

        super.onDestroy()
    }

    companion object {
        private const val READ_HEART_RATE_PERMISSION = "android.permission.health.READ_HEART_RATE"
    }
}

@Composable
fun WearApp(greetingName: String) {
    HealthMonitoringTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            val transformationSpec = rememberTransformationSpec()

            TransformingLazyColumn(
                state = listState
            ) {
                item {
                    ListHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec)
                            .padding(20.dp),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Text(text = stringResource(R.string.hello_world, greetingName))
                    }
                }
            }
        }
    }
}