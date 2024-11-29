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
import com.ag.projects.aatask.databinding.FragmentPrayerTimesBinding
import com.ag.projects.aatask.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrayerTimesFragment : Fragment() {

    private lateinit var binding: FragmentPrayerTimesBinding

    private val viewModel: PrayerTimeFragmentViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrayerTimesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission()

        getPrayerTimes()

//        requestPermissionLauncher.launch(
//            arrayOf(
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//            )
//        )
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
                        "Location permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                    getPrayerTimes()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun getPrayerTimes() {

        viewModel.getPrayerTimes(
            year = 2017,
            month = 4,
            latitude = 51.508515,
            longitude = 51.508515
        )

      lifecycleScope.launch(Dispatchers.IO) {
          viewModel.prayerTimes.collectLatest {
              when(it){
                  is Result.Error -> {}
                  Result.Loading ->{}
                  is Result.Success ->{
                      Log.d("the data is ","${it.data.code}")
                  }
              }
          }
      }
    }

}