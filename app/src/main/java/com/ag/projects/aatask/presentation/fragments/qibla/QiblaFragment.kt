package com.ag.projects.aatask.presentation.fragments.qibla

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ag.projects.aatask.databinding.FragmentQiblaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QiblaFragment : Fragment() {

    private lateinit var binding:FragmentQiblaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  =FragmentQiblaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}