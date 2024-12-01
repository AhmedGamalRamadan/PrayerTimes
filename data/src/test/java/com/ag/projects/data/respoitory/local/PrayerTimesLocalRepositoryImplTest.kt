package com.ag.projects.data.respoitory.local

import com.ag.projects.data.local.PrayerTimeDao
import com.ag.projects.data.local.PrayerTimesDataBase
import com.ag.projects.fake.dataEntityList
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PrayerTimesLocalRepositoryImplTest {

    private lateinit var dao: PrayerTimeDao
    private lateinit var localRepositoryImpl: PrayerTimesLocalRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)

        // Mock the database and return the mocked DAO
         val dataBase = mockk<PrayerTimesDataBase> {
            coEvery { dao() } returns dao
        }
        localRepositoryImpl = PrayerTimesLocalRepositoryImpl(dataBase)
    }

    @Test
    fun `if items inserted return true`() = runBlocking{

        // Mock behavior for the DAO methods
        coEvery { dao.insertData(any()) } returns Unit
        coEvery { dao.getAllData() } returns dataEntityList

        // Perform actions
        localRepositoryImpl.insertData(dataEntityList)
        val result = localRepositoryImpl.getAllData()

        // Assert the results
        assertEquals(dataEntityList, result)
    }
}