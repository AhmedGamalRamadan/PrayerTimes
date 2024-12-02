package com.ag.projects.data.respoitory.remote

import com.ag.projects.data.remote.PrayerTimesService
import com.ag.projects.fake.lat
import com.ag.projects.fake.lng
import com.ag.projects.fake.month
import com.ag.projects.fake.prayerTimesSuccessResponse
import com.ag.projects.fake.year
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PrayerTimeRepositoryImplTest {


    private val mockWebServer = MockWebServer()
    private lateinit var prayerTimesService: PrayerTimesService
    private lateinit var repositoryImpl: PrayerTimeRepositoryImpl


    @Before
    fun setUp() {
        prayerTimesService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimesService::class.java)
        repositoryImpl = PrayerTimeRepositoryImpl(prayerTimesService)
    }

    @Test
    fun `if get prayer times response successfully return true`() = runBlocking {

        val response = MockResponse()
            .setResponseCode(200)
            .setBody(prayerTimesSuccessResponse)

        mockWebServer.enqueue(response)

        val result = repositoryImpl.getPrayerTimes(
            year = year,
            month = month,
            latitude = lat,
            longitude = lng
        )

        assertEquals(2, result.data.size)
    }

    @Test(expected = HttpException::class)
    fun `if exception happen when get prayer times response return true`()= runBlocking {

        val response = MockResponse()
            .setResponseCode(400)
            .setBody("BAD_REQUEST")

        mockWebServer.enqueue(response)

        val result = repositoryImpl.getPrayerTimes(
            year = year,
            month = month,
            latitude = 0.0,
            longitude = 0.0
        )

        assertEquals(400,result.code)
    }
}