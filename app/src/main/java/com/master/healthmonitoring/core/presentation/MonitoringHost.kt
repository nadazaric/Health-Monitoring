package com.master.healthmonitoring.core.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.master.healthmonitoring.consts.Permissions
import com.master.healthmonitoring.core.service.SensorTrackingService

@Composable
fun MonitoringHost(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val permission = getHeartRatePermission()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            SensorTrackingService.start(context.applicationContext)
        }
    }

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            SensorTrackingService.start(context.applicationContext)
        } else {
            permissionLauncher.launch(permission)
        }
    }

    content()
}

private fun getHeartRatePermission(): String {
    return if (Build.VERSION.SDK_INT >= 36) {
        Permissions.READ_HEART_RATE_PERMISSION
    } else {
        Manifest.permission.BODY_SENSORS
    }
}