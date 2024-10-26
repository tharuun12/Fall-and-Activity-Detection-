package com.proiect.proiect_java

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ActivityDetectionActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var tflite: Interpreter

    private val inputBuffer = Array(1) { Array(128) { FloatArray(6) } }
    private var timeStepCount = 0

    private lateinit var sensorDataView: TextView
    private lateinit var activityPredictionView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection)

        sensorDataView = findViewById(R.id.sensor_data)
        activityPredictionView = findViewById(R.id.activity_prediction)

        // Initialize the sensor manager and sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

        // Load TensorFlow Lite model
        tflite = Interpreter(loadModelFile())
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    updateSensorData(event, 0) // First 3 features for accelerometer
                }
                Sensor.TYPE_GYROSCOPE -> {
                    updateSensorData(event, 3) // Next 3 features for gyroscope
                }
            }
        }
    }

    private fun updateSensorData(event: SensorEvent, offset: Int) {
        // Update sensor data in the input buffer
        for (i in 0..2) {
            inputBuffer[0][timeStepCount][offset + i] = event.values[i]
        }
        timeStepCount++

        if (timeStepCount == 128) {
            predictActivity()
            timeStepCount = 0
        }

        displaySensorData(event)
    }

    private fun predictActivity() {
        // Prepare the output buffer
        val outputBuffer = Array(1) { FloatArray(6) }

        // Run inference with the TensorFlow Lite model
        tflite.run(inputBuffer, outputBuffer)

        // Display the predicted activity
        val predictedActivityIndex = outputBuffer[0].indices.maxByOrNull { outputBuffer[0][it] } ?: -1
        val activities = arrayOf("Walking", "Walking Upstairs", "Walking Downstairs", "Sitting", "Standing", "Lying")
        activityPredictionView.text = "Predicted Activity: ${activities[predictedActivityIndex]}"
    }

    private fun displaySensorData(event: SensorEvent) {
        val sensorData = "X: ${event.values[0]}, Y: ${event.values[1]}, Z: ${event.values[2]}"
        sensorDataView.text = "Sensor Data: $sensorData"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing for now
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = assets.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}