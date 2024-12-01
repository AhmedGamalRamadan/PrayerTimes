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
import androidx.recyclerview.widget.RecyclerView
import com.ag.projects.aatask.R
import com.ag.projects.aatask.databinding.FragmentPrayerTimesBinding
import com.ag.projects.aatask.presentation.adapter.PrayerTimesAdapter
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.formatDuration
import com.ag.projects.aatask.util.helper.getAddressFromLatLng
import com.ag.projects.aatask.util.helper.getNextPrayerTime
import com.ag.projects.aatask.util.helper.toMap
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

    private var prayerTimesListSize = 0

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
        setupPrayerTimesRV()
        checkLocationPermission()

        fetchLocalData()
    }

    private fun initBinding() {
        binding.apply {

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

    private fun setupPrayerTimesRV() {
        binding.rvPrayerTimes.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = prayerTimesAdapter

            addRVListener()
        }
    }

    private fun addRVListener() {
        binding.rvPrayerTimes.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                currentPosition = firstVisibleItem
                observePrayerTimes()
            }
        })
    }

    private fun navigateToQibla() {
        findNavController().navigate(R.id.action_homeFragment_to_qiblaFragment)
    }

    private fun getNextPrayerDay() {
        if (currentPosition < prayerTimesListSize - 1) {
            currentPosition++
            binding.rvPrayerTimes.smoothScrollToPosition(currentPosition)
            observePrayerTimes()
        }
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
        Log.d("InsertPrayerTimesUseCase", "enter get user location")

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

                getPrayerTimes()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.can_not_get_location),
                Toast.LENGTH_SHORT
            ).show()
            Log.d("prayer fragment location is ", "failure $it")
            return@addOnFailureListener
        }.addOnCanceledListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.can_not_get_location),
                Toast.LENGTH_SHORT
            ).show()
            Log.d("prayer fragment location is ", "Canceled")
            return@addOnCanceledListener
        }
    }

    private fun fetchLocalData() {
        lifecycleScope.launch {
            if (viewModel.localDataExist()) {
                getPrayerTimes()
            }
        }
    }

    private fun getPrayerTimes() {
        Log.d("InsertPrayerTimesUseCase", "enter get prayer times from fragment")

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
        Log.d("InsertPrayerTimesUseCase", "enter observer from fragment")

        lifecycleScope.launch {
            viewModel.prayerTimes.collectLatest { prayerTimesResponse ->
                when (prayerTimesResponse) {
                    is Result.Error -> {
                        hideProgressbar()
                    }

                    Result.Loading -> {
                        showProgressbar()
                    }

                    is Result.Success -> {
                        hideProgressbar()
                        val prayerDataList = prayerTimesResponse.data.data
                        prayerTimesAdapter.syncListDiffer.submitList(prayerDataList)
                        Log.d("the data is ", "${prayerTimesResponse.data.data}")

                        prayerTimesListSize = prayerDataList.size
                        val currentDay = prayerDataList[currentPosition]
                        binding.tvDate.text = currentDay.date?.readable

                        // Get cleaned prayer timings
                        val prayerTimings = currentDay.timings.toMap()
                        val timezone = currentDay.meta?.timezone

                        // Calculate the next prayer
                        val nextPrayer = timezone?.let { getNextPrayerTime(prayerTimings, it) }

                        // Update the UI with the next prayer info
                        if (nextPrayer != null) {
                            binding.tvNextPrayerName.text = nextPrayer.first
                            val formattedTimeLeft = formatDuration(nextPrayer.second)
                            binding.tvPrayerTimeLeft.text =
                                getString(R.string.time_left, formattedTimeLeft)
                        } else {
                            binding.tvNextPrayerName.text =
                                getString(R.string.no_more_prayers_today)
                            binding.tvPrayerTimeLeft.text = ""
                        }

                    }
                }
            }

        }
    }

    private fun hideProgressbar() {
        binding.progressbar.visibility = View.GONE
    }

    private fun showProgressbar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    fun Map<String, String>.toCleanedPrayerTimings(): Map<String, String> {
        return this.mapValues { entry ->
            entry.value.split(" ")[0] // Extract "HH:mm" part
        }
    }
}