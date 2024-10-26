package com.proiect.proiect_java

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.math.BigDecimal
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.proiect.proiect_java.databinding.ActivityMainBinding
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    var threadObj: ThreadClass = ThreadClass()

    var graph: GraphView? = null
    private var mSeries1: LineGraphSeries<DataPoint>? = null
    var pointsInd: Int = 0
    private var staticmAccelVal = FloatArray(3)
    var inflater: LayoutInflater? = null
    var popupView: View? = null
    var popupWindow: PopupWindow? = null
    private var booleanShowPopUp = false
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newButton.setOnClickListener {
            val intent = Intent(this, ActivityDetectionActivity::class.java)
            startActivity(intent)
        }

        threadObj.start()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager!!.registerListener(this, sensor, 20000)

        graph = findViewById<View>(R.id.idGraphView) as GraphView

        binding.button.setOnClickListener { threadObj.state = ThreadClass.State.RECOVERY }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mSeries1 = LineGraphSeries()
        graph!!.addSeries(mSeries1)
        graph!!.viewport.isXAxisBoundsManual = true
        graph!!.viewport.setMinX(0.0)
        graph!!.viewport.setMaxX(1024.0)

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
                return
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location
            } else {
                // Permission Denied
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    val location: Unit
        get() {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            var myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (myLocation == null) {
                myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            }
        }

    fun lpf(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) {
            return input
        }
        for (i in input.indices) {
            output[i] = (output[i] + ALPHA * (input[i] - output[i])).toFloat()
        }
        return output
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            staticmAccelVal = lpf(event.values.clone(), staticmAccelVal)

            // Convert to Float and use Float.pow for calculations
            threadObj.mAccelVal = (sqrt(
                staticmAccelVal[0].toDouble().pow(2.0) +
                        staticmAccelVal[1].toDouble().pow(2.0) +
                        staticmAccelVal[2].toDouble().pow(2.0)
            ))

            threadObj.mAccelVal = BigDecimal.valueOf(threadObj.mAccelVal)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .toDouble()

            if (threadObj.state == ThreadClass.State.SEND_POPUP && !booleanShowPopUp) {
                inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                popupView = inflater!!.inflate(R.layout.popup_window, null)

                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true
                popupWindow = PopupWindow(popupView, width, height, focusable)

                popupWindow!!.showAtLocation(
                    window.decorView.findViewById(android.R.id.content),
                    Gravity.CENTER,
                    0,
                    0
                )

                popupView?.let { view ->
                    val btn1 = view.findViewById<Button>(R.id.button2)
                    btn1.setOnClickListener {
                        popupWindow!!.dismiss()
                        threadObj.state = ThreadClass.State.NORMAL
                        booleanShowPopUp = false
                    }
                }

                threadObj.latitude = latitude
                threadObj.longitude = longitude
                booleanShowPopUp = true

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient!!.lastLocation
                    .addOnSuccessListener(this) { location ->
                        if (location != null) {
                            threadObj.latitude = location.latitude
                            threadObj.longitude = location.longitude
                        } else {
                            println("Not working..")
                        }
                    }
            } else if (threadObj.state == ThreadClass.State.UNCONSCIOUS) {
                popupWindow!!.dismiss()
                booleanShowPopUp = false
            } else {
            }

            binding.button.text = "" + threadObj.state

            mSeries1!!.appendData(
                DataPoint(pointsInd++.toDouble(), threadObj.mAccelVal),
                true,
                1024
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        private const val ALPHA = 0.1
    }
}