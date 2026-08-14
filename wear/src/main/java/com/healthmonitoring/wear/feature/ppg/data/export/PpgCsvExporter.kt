package com.healthmonitoring.wear.feature.ppg.data.export

import android.content.Context
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgCsvExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun export(
        measurements: List<PpgMeasurement>
    ): File = withContext(Dispatchers.IO) {
        val exportDirectory = File(
            context.filesDir,
            EXPORT_DIRECTORY_NAME
        )

        if (!exportDirectory.exists()) {
            exportDirectory.mkdirs()
        }

        val fileTimestamp = SimpleDateFormat(
            FILE_DATE_FORMAT,
            Locale.US
        ).format(Date())

        val csvFile = File(
            exportDirectory,
            "ppg_$fileTimestamp.csv"
        )

        csvFile.bufferedWriter().use { writer ->
            writer.appendLine(CSV_HEADER)

            measurements.forEachIndexed { index, measurement ->
                writer.appendLine(
                    buildString {
                        append(index)
                        append(CSV_SEPARATOR)
                        append(measurement.timestamp)
                        append(CSV_SEPARATOR)
                        append(measurement.green)
                        append(CSV_SEPARATOR)
                        append(measurement.greenStatus)
                        append(CSV_SEPARATOR)
                        append(measurement.red)
                        append(CSV_SEPARATOR)
                        append(measurement.redStatus)
                        append(CSV_SEPARATOR)
                        append(measurement.infrared)
                        append(CSV_SEPARATOR)
                        append(measurement.infraredStatus)
                    }
                )
            }
        }

        csvFile
    }

    private companion object {

        const val EXPORT_DIRECTORY_NAME = "ppg_measurements"

        const val FILE_DATE_FORMAT = "yyyyMMdd_HHmmss"

        const val CSV_SEPARATOR = ","

        const val CSV_HEADER =
            "sample_index,timestamp,green,green_status," +
                    "red,red_status,infrared,infrared_status"
    }
}