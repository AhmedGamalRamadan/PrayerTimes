package com.ag.projects.aatask.presentation.fragments.qibla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.handleRequest
import com.ag.projects.domain.model.QiblaResponse
import com.ag.projects.domain.usecase.qibla.GetQiblaDirectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QiblaFragmentViewModel @Inject constructor(
    private val getQiblaDirectionUseCase: GetQiblaDirectionUseCase
):ViewModel(){


    private val _qiblaDirection = MutableStateFlow<Result<QiblaResponse>>(Result.Loading)
    val qiblaDirection = _qiblaDirection.asStateFlow()

    init {
        getQiblaDirection()
    }
    private fun getQiblaDirection(){
        viewModelScope.launch {
            handleRequest(
                request = {
                    getQiblaDirectionUseCase()
                },
                state = _qiblaDirection
            )
        }
    }

}