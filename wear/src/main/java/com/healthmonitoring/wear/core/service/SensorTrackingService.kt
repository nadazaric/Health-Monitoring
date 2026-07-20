package com.healthmonitoring.wear.core.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.feature.heartrate.domain.use_case.HeartRateUseCases
import com.healthmonitoring.wear.feature.skin_temperature.domain.use_case.SkinTemperatureUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SensorTrackingService : Service() {

    @Inject
    lateinit var healthTrackerProvider: HealthTrackerProvider

    @Inject
    lateinit var heartRateUseCases: HeartRateUseCases

    @Inject
    lateinit var skinTemperatureUseCases: SkinTemperatureUseCases

    private var isTrackingStarted = false
    private var areSensorsStarted = false
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        when (intent?.action) {
            ACTION_START_SERVICE -> startSensorTrackingService()
            ACTION_STOP_SERVICE -> stopSensorTrackingService()
        }

        return START_STICKY
    }

    private fun startSensorTrackingService() {
        if (isTrackingStarted) {
            return
        }

        Log.d(Tags.SENSOR_TRACKING_SERVICE, "Starting sensor tracking service.")

        isTrackingStarted = true

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            createNotification(),
            resolveForegroundServiceType()
        )

        acquireWakeLock()

        healthTrackerProvider.setOnConnectedCallback {
            startContinuousSensors()
        }

        if (healthTrackerProvider.isConnected()) {
            startContinuousSensors()
        } else {
            healthTrackerProvider.connect()
        }
    }

    private fun stopSensorTrackingService() {
        Log.d(Tags.SENSOR_TRACKING_SERVICE, "Stopping sensor tracking service.")

        heartRateUseCases.stopHeartRateTracking()
        skinTemperatureUseCases.stopSkinTemperatureTracking()

        healthTrackerProvider.disconnect()
        releaseWakeLock()

        areSensorsStarted = false
        isTrackingStarted = false

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification() =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_heart)
            .setContentTitle("Health monitoring active")
            .setContentText("Continuous sensor tracking is running.")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(Tags.SENSOR_TRACKING_SERVICE, "Sensor tracking service destroyed.")

        heartRateUseCases.stopHeartRateTracking()
        skinTemperatureUseCases.stopSkinTemperatureTracking()

        healthTrackerProvider.disconnect()
        releaseWakeLock()

        areSensorsStarted = false
        isTrackingStarted = false

        super.onDestroy()
    }

    private fun resolveForegroundServiceType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
        } else {
            0
        }
    }

    private fun startContinuousSensors() {
        if (areSensorsStarted) {
            return
        }

        Log.d(
            Tags.SENSOR_TRACKING_SERVICE,
            "Starting continuous sensors."
        )

        areSensorsStarted = true

        heartRateUseCases.startHeartRateTracking()
        skinTemperatureUseCases.startSkinTemperatureTracking()
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) {
            return
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "$packageName:SensorTrackingWakeLock"
        ).apply {
            setReferenceCounted(false)
            acquire()
        }
    }

    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }

        wakeLock = null
    }

    companion object {
        const val ACTION_START_SERVICE = "com.healthmonitoring.wear.START_SENSOR_TRACKING"
        const val ACTION_STOP_SERVICE = "com.healthmonitoring.wear.STOP_SENSOR_TRACKING"

        private const val NOTIFICATION_ID = 1001
        private const val NOTIFICATION_CHANNEL_ID = "sensor_tracking_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Sensor Tracking"

        fun start(context: Context) {
            val intent = Intent(context, SensorTrackingService::class.java).apply {
                action = ACTION_START_SERVICE
            }

            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, SensorTrackingService::class.java).apply {
                action = ACTION_STOP_SERVICE
            }

            context.startService(intent)
        }
    }
}