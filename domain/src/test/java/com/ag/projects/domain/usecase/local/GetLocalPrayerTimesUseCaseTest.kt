package com.ag.projects.domain.usecase.local

import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository
import com.ag.projects.fake.dataEntityList
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLocalPrayerTimesUseCaseTest {

    private lateinit var localRepository: PrayerTimesLocalRepository
    private lateinit var getLocalPrayerTimesUseCase: GetLocalPrayerTimesUseCase

    @Before
    fun setUp() {
        localRepository = mockk()
        getLocalPrayerTimesUseCase = GetLocalPrayerTimesUseCase(localRepository)
    }

    @Test
    fun `if get prayer times locally return true`() = runBlocking {

        coEvery {
            localRepository.getAllData()
        } coAnswers {
            dataEntityList
        }

        val result = getLocalPrayerTimesUseCase()

        assertEquals(dataEntityList.size, result.size)
    }

    @Test
    fun `if can not get prayer times locally return true`() = runBlocking {

        coEvery {
            localRepository.getAllData()
        } coAnswers {
            emptyList()
        }

        val result = getLocalPrayerTimesUseCase()

        assertEquals(0, result.size)
    }
}