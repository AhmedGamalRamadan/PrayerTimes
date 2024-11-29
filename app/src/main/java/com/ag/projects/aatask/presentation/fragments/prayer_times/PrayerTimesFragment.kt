package com.ag.projects.aatask.presentation.fragments.prayer_times

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ag.projects.aatask.R
import com.ag.projects.aatask.databinding.FragmentPrayerTimesBinding
import com.ag.projects.aatask.presentation.adapter.PrayerTimesAdapter
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.getAddressFromLatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class PrayerTimesFragment : Fragment() {

    private lateinit var binding: FragmentPrayerTimesBinding

    private val viewModel: PrayerTimeFragmentViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentPosition = 0
    private var latitude = 0.0
    private var longitude = 0.0

    private val prayerTimesAdapter by lazy {
        PrayerTimesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrayerTimesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        initBinding()
        checkLocationPermission()

    }

    private fun initBinding() {
        binding.apply {
            rvPrayerTimes.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL, false
                )
                adapter = prayerTimesAdapter
            }
            btnNavigateToQibla.setOnClickListener {
                navigateToQibla()
            }

            btnNextTimes.setOnClickListener {
                getNextPrayerDay()
            }

            btnPreviousTimes.setOnClickListener {
                getPreviousPrayerDay()
            }
        }

    }

    private fun navigateToQibla() {
        findNavController().navigate(R.id.action_homeFragment_to_qiblaFragment)
    }

    private fun getNextPrayerDay() {
        currentPosition++
        binding.rvPrayerTimes.smoothScrollToPosition(currentPosition)
        observePrayerTimes()
    }

    private fun getPreviousPrayerDay() {
        if (currentPosition > 0) {
            currentPosition--
            binding.rvPrayerTimes.smoothScrollToPosition(currentPosition)
            observePrayerTimes()
        }
    }

    private fun checkLocationPermission() {

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineLocationGranted =
                    permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseLocationGranted =
                    permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineLocationGranted || coarseLocationGranted) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_permission_granted),
                        Toast.LENGTH_SHORT
                    ).show()
//                    getPrayerTimes()
                    getUserLocation()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        if (isLocationPermissionGranted()) {
//            getPrayerTimes()
            getUserLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

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
                latitude = location.latitude
                longitude = location.longitude

                val userLocation =
                    getAddressFromLatLng(
                        requireContext(),
                        location.latitude,
                        location.longitude
                    )

                binding.tvUserLocation.text = userLocation
            }
            getPrayerTimes()
        }
    }

    private fun getPrayerTimes() {


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        viewModel.getPrayerTimes(
            year = year,
            month = month,
            latitude = latitude,
            longitude = longitude
        )
        observePrayerTimes()
    }

    private fun observePrayerTimes() {
        lifecycleScope.launch {
            viewModel.prayerTimes.collectLatest { prayerTimesResponse ->
                when (prayerTimesResponse) {
                    is Result.Error -> {}
                    Result.Loading -> {}
                    is Result.Success -> {
                        prayerTimesAdapter.syncListDiffer.submitList(prayerTimesResponse.data.data)
                        Log.d("the data is ", "${prayerTimesResponse.data.data}")

                        val currentDay = prayerTimesResponse.data.data[currentPosition]
                        binding.tvDate.text = currentDay.date.readable
                    }
                }
            }

        }
    }

}