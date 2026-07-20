package com.healthmonitoring.wear.core.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.healthmonitoring.wear.consts.Permissions
import com.healthmonitoring.wear.core.service.SensorTrackingService

@Composable
fun MonitoringHost(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val requiredPermissions = getRequiredSensorPermissions()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val areAllPermissionsGranted = requiredPermissions.all { permission ->
            permissionResults[permission] == true
        }

        if (areAllPermissionsGranted) {
            SensorTrackingService.start(context.applicationContext)
        }
    }

    LaunchedEffect(Unit) {
        val areAllPermissionsGranted = requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (areAllPermissionsGranted) {
            SensorTrackingService.start(context.applicationContext)
        } else {
            permissionLauncher.launch(requiredPermissions)
        }
    }

    content()
}

private fun getRequiredSensorPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= 36) {
        arrayOf(
            Permissions.READ_HEART_RATE_PERMISSION,
            Permissions.READ_SKIN_TEMPERATURE_PERMISSION,
            Permissions.READ_OXYGEN_SATURATION_PERMISSION
        )
    } else {
        arrayOf(
            Manifest.permission.BODY_SENSORS
        )
    }
}