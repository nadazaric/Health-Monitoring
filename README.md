# Health Monitoring

Health Monitoring is a Wear OS application for reading and displaying sensor data from a Samsung Galaxy Watch.

## Samsung Health Sensor SDK Setup

This project uses the Samsung Health Sensor SDK to access sensor data from a Samsung Galaxy Watch.

The SDK can be downloaded from the official Samsung Developer website: [Samsung Health Sensor SDK](https://developer.samsung.com/health/sensor/overview.html).

After downloading and extracting the SDK, locate the following file: ```samsung-health-sensor-api-XXX.aar```

Copy the `.aar` file into the following project folder: ```app/libs/```

## Gradle Configuration

The SDK path is defined in `app/build.gradle.kts`:

```kotlin
val samsungHealthSensorSdkPath = "libs/samsung-health-sensor-api-1.4.1.aar"
```

The SDK is added as a local Gradle dependency:

```kotlin
dependencies {
    implementation(files(samsungHealthSensorSdkPath))
}
```

If the SDK file name or location changes, only the value of `samsungHealthSensorSdkPath` needs to be updated.
