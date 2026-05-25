package com.master.healthmonitoring.feature.heartrate.presentation.component

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.master.healthmonitoring.R
import com.master.healthmonitoring.consts.Permissions
import com.master.healthmonitoring.feature.heartrate.presentation.HeartRateEvent
import com.master.healthmonitoring.feature.heartrate.presentation.HeartRateState
import com.master.healthmonitoring.feature.heartrate.presentation.HeartRateViewModel

@Composable
fun HeartRateCard(
    modifier: Modifier = Modifier,
    viewModel: HeartRateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.onEvent(HeartRateEvent.StartTracking)
            } else {
                viewModel.onEvent(HeartRateEvent.PermissionDenied)
            }
        }

    LaunchedEffect(Unit) {
        val permission = getHeartRatePermission()

        val isGranted =
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            viewModel.onEvent(HeartRateEvent.StartTracking)
        } else {
            permissionLauncher.launch(permission)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(HeartRateEvent.StopTracking)
        }
    }

    HeartRateCardContent(
        state = state,
        modifier = modifier
    )
}

@Composable
private fun HeartRateCardContent(
    state: HeartRateState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(
                horizontal = 18.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(34.dp),
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = "Heart rate",
            tint = Color.White
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = getHeartRateText(state),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
    }
}

private fun getHeartRateText(
    state: HeartRateState
): String {
    return if (state.bpm != null) {
        "${state.bpm} BPM"
    } else {
        "-- BPM"
    }
}

private fun getHeartRatePermission(): String {
    return if (Build.VERSION.SDK_INT >= 36) {
        Permissions.READ_HEART_RATE_PERMISSION
    } else {
        Manifest.permission.BODY_SENSORS
    }
}