package com.ag.projects.aatask.presentation.fragments.prayer_times

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ag.projects.aatask.databinding.FragmentPrayerTimesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrayerTimesFragment : Fragment() {

    private lateinit var binding:FragmentPrayerTimesBinding

    private val viewModel:PrayerTimeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentPrayerTimesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}