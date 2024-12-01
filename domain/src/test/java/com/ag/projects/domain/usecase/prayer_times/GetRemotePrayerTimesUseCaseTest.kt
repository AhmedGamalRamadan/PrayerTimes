package com.ag.projects.domain.usecase.prayer_times

import com.ag.projects.domain.repository.remote.PrayerTimeRepository
import com.ag.projects.fake.emptyPrayerTimesResponse
import com.ag.projects.fake.lat
import com.ag.projects.fake.lng
import com.ag.projects.fake.month
import com.ag.projects.fake.successPrayerTimesResponse
import com.ag.projects.fake.year
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetRemotePrayerTimesUseCaseTest {

    private lateinit var remoteRepository: PrayerTimeRepository
    private lateinit var getRemotePrayerTimesUseCase: GetRemotePrayerTimesUseCase

    @Before
    fun setUp() {
        remoteRepository = mockk()
        getRemotePrayerTimesUseCase = GetRemotePrayerTimesUseCase(remoteRepository)
    }

    @Test
    fun `if get prayer times from api return true`() = runBlocking {

        coEvery {
            remoteRepository.getPrayerTimes(
                year = year,
                month = month,
                latitude = lat,
                longitude = lng
            )
        } coAnswers {
            successPrayerTimesResponse
        }

        val response = getRemotePrayerTimesUseCase(
            year = year,
            month = month,
            latitude = lat,
            longitude = lng
        )

        assertEquals(successPrayerTimesResponse,response)
    }

    @Test
    fun `if prayer times response empty return true`()= runBlocking {
        coEvery {
            remoteRepository.getPrayerTimes(
                year = year,
                month = month,
                latitude = lat,
                longitude = lng
            )
        } coAnswers {
            emptyPrayerTimesResponse
        }

        val response = getRemotePrayerTimesUseCase(
            year = year,
            month = month,
            latitude = lat,
            longitude = lng
        )

        assertEquals(true,response.data.isEmpty())
    }
}