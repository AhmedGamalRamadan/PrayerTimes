package com.ag.projects.aatask.presentation.fragments.prayer_times

import com.ag.projects.aatask.fake.month
import com.ag.projects.aatask.fake.prayerTimesSuccessResponse
import com.ag.projects.aatask.fake.year
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.network.NetworkConnection
import com.ag.projects.domain.usecase.local.ClearPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.GetLocalPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.InsertPrayerTimesUseCase
import com.ag.projects.domain.usecase.prayer_times.GetRemotePrayerTimesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class PrayerTimeFragmentViewModelTest {

    private lateinit var getRemotePrayerTimesUseCase: GetRemotePrayerTimesUseCase
    private lateinit var insertPrayerTimesUseCase: InsertPrayerTimesUseCase
    private lateinit var getLocalPrayerTimesUseCase: GetLocalPrayerTimesUseCase
    private lateinit var clearPrayerTimesUseCase: ClearPrayerTimesUseCase

    private lateinit var networkConnection: NetworkConnection
    private lateinit var viewModel: PrayerTimeFragmentViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        getRemotePrayerTimesUseCase = mockk()
        insertPrayerTimesUseCase = mockk()
        getLocalPrayerTimesUseCase = mockk()
        clearPrayerTimesUseCase = mockk()

        networkConnection = mockk()

        viewModel = PrayerTimeFragmentViewModel(
            getRemotePrayerTimesUseCase,
            insertPrayerTimesUseCase,
            getLocalPrayerTimesUseCase,
            clearPrayerTimesUseCase,
            networkConnection
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `if fail to get prayer times response return true`() = runBlocking {

        coEvery { networkConnection.isWifiConnected() } returns false


        coEvery {
            getRemotePrayerTimesUseCase(
                year = year,
                month = month,
                latitude = 0.0,
                longitude = 0.0
            )
        }returns prayerTimesSuccessResponse

       viewModel.getPrayerTimes(
            year = year,
            month = month,
            latitude = 0.0,
            longitude = 0.0
        )

        var result =0
        viewModel.prayerTimes.collectLatest {
            when(it){
                is Result.Error -> {}
                Result.Loading ->{}
                is Result.Success -> {
                    result =it.data.code
                    cancel()
                }
            }
        }

        assertEquals(200,result)
    }
}