package com.ag.projects.data.local

import com.ag.projects.fake.dataEntityList
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PrayerTimeDaoTest {

    private lateinit var dao: PrayerTimeDao

    @Before
    fun setUp() {
        dao = mockk()
        coEvery {
            dao.insertData(any())
        } just Runs
    }

    @Test
    fun `if items inserted to database return true`() = runBlocking {

        coEvery { dao.getAllData() } coAnswers {
            dataEntityList
        }
        dao.insertData(dataEntityList)
        val result = dao.getAllData()
        TestCase.assertEquals(dataEntityList, result)
    }

    @Test
    fun `if items not inserted return false`() = runBlocking {

        coEvery {
            dao.getAllData()
        } returns emptyList()

        TestCase.assertEquals(dao.getAllData().isEmpty(), true)
    }

    @Test
    fun `if database cleared return true`() = runBlocking {
        coEvery { dao.clearDatabase() } just Runs

        dao.insertData(dataEntityList)
        dao.clearDatabase()
        coEvery { dao.getAllData() } returns emptyList()

        TestCase.assertEquals(true, dao.getAllData().isEmpty())
    }
}
