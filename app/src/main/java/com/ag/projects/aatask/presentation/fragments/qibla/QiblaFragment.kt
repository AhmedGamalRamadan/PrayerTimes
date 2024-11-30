package com.ag.projects.aatask.presentation.fragments.qibla

import android.annotation.SuppressLint
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ag.projects.aatask.R
import com.ag.projects.aatask.databinding.FragmentQiblaBinding
import com.ag.projects.aatask.util.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QiblaFragment : Fragment(), OnMapReadyCallback, SensorEventListener {

    private lateinit var binding: FragmentQiblaBinding

    private val viewModel: QiblaFragmentViewModel by viewModels()

    private var userLocation: LatLng? = null

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var sensorManager: SensorManager

    private var azimuth: Float = 0f
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQiblaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleMap()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private fun initGoogleMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true

        getUserLocation()
        getQiblaLocation()
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        Log.d(
            "SensorEvent",
            "Sensor type: ${event.sensor.type}, Values: ${event.values.contentToString()}"
        )

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values
        }

        Log.d("SensorEvent", "the gravity is $gravity and the geomagnetic is $geomagnetic")

        if (gravity != null && geomagnetic != null) {
            val R = FloatArray(9)
            val I = FloatArray(9)

//            Log.d("SensorEvent", "enter if statement")

//            Log.d("SensorEvent", "enter if rotation matrix")

            val rotationMatrixSuccess = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)

            if (rotationMatrixSuccess) {
                Log.d("SensorEvent", "Rotation matrix successfully calculated")

                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360) % 360 // Normalize azimuth to [0, 360]

                binding.ivDirection.visibility = View.VISIBLE
                // Update compass icon rotation
                binding.ivDirection.rotation = -azimuth
                Log.d("SensorEvent", "the azimuth is $azimuth")

                // Optionally update map bearing (camera rotation)
//                updateMapDirection(azimuth)
            } else {
                Log.d("SensorEvent", "Failed to calculate rotation matrix.")
            }
        } else {
            Log.d("SensorEvent", "Gravity or Geomagnetic data is null")
        }
    }

    private fun updateDirection(azimuth: Float) {
        binding.ivDirection.rotation = azimuth
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {

                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.addMarker(
                    MarkerOptions().position(currentLatLng).title(getString(R.string.your_location))
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

                userLocation = currentLatLng

            }
        }
    }

    private fun getQiblaLocation() {

        lifecycleScope.launch {

            viewModel.qiblaDirection.collectLatest { qiblaDirection ->
                when (qiblaDirection) {
                    is Result.Error -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.can_not_get_direction),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Result.Loading -> {}

                    is Result.Success -> {
                        val qiblaLatitude = qiblaDirection.data.data.latitude
                        val qiblaLongitude = qiblaDirection.data.data.longitude
                        val qiblaLatLng = LatLng(qiblaLatitude, qiblaLongitude)

                        qiblaLatLng?.let { qibla ->
                            map.addMarker(
                                MarkerOptions()
                                    .position(qibla)
                                    .icon(bitmapFromVector(R.drawable.ic_kaaba))
                            )

                            userLocation?.let { end ->
                                drawLineBetweenLocations(
                                    start = qibla,
                                    end = end
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    private fun drawLineBetweenLocations(start: LatLng, end: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(start, end)
            .color(android.graphics.Color.BLUE)
            .width(5f)
        map.addPolyline(polylineOptions)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun bitmapFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = resources.getDrawable(vectorResId, null)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            Log.d("SensorStatus", "Accelerometer registered")
        } else {
            Log.e("SensorStatus", "Accelerometer not available")
        }

        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
            Log.d("SensorStatus", "Magnetometer registered")
        } else {
            Log.e("SensorStatus", "Magnetometer not available")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}