package com.ag.projects.domain.usecase.qibla

import com.ag.projects.domain.repository.remote.PrayerTimeRepository
import com.ag.projects.fake.failQiblaResponse
import com.ag.projects.fake.successQiblaResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetQiblaDirectionUseCaseTest {

    private lateinit var remoteRepository: PrayerTimeRepository
    private lateinit var getQiblaDirectionUseCase: GetQiblaDirectionUseCase

    @Before
    fun setUp() {
        remoteRepository = mockk()
        getQiblaDirectionUseCase = GetQiblaDirectionUseCase(remoteRepository)
    }

    @Test
    fun `if get qibla direction from api return true`() = runBlocking {

        coEvery {
            remoteRepository.getQiblaDirection()
        } coAnswers {
            successQiblaResponse
        }

        val response = getQiblaDirectionUseCase()

        TestCase.assertEquals(successQiblaResponse.status, response.status)
    }

    @Test
    fun `if fail to get qibla direction return true`() = runBlocking {
        coEvery {
            remoteRepository.getQiblaDirection()
        } coAnswers {
            failQiblaResponse
        }

        val response = getQiblaDirectionUseCase()

        TestCase.assertEquals(failQiblaResponse.status, response.status)
    }
}