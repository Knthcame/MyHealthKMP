package com.knthcame.myhealthkmp.data.dashboard.repositories

import app.cash.turbine.test
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import dev.mokkery.answering.returns
import dev.mokkery.answering.returnsBy
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DefaultDashboardRepositoryTest {
    private lateinit var dashboardRepository: DefaultDashboardRepository

    private val diaryDao = mock<DiaryDao>()
    private val dateTimeRepository = mock<DateTimeRepository> {
        every { now } returnsBy { Clock.System.now() }
    }

    @Test
    fun getCurrentHeartRate_returnsNull_whenNoData() = runTest {
        every { diaryDao.heartRates } returns flowOf(emptyList())
        initRepository()

        dashboardRepository.currentHeartRate.test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getCurrentHeartRate_returnsHeartRate_whenSingleMeasurementAvailable() = runTest {
        val expected = HeartRate(80.0, Clock.System.now())
        every { diaryDao.heartRates } returns flowOf(listOf(expected))
        initRepository()

        dashboardRepository.currentHeartRate.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getCurrentHeartRate_returnsLatestHeartRate_whenMultipleMeasurementsAvailable() = runTest {
        val expected = HeartRate(80.0, Clock.System.now())
        every { diaryDao.heartRates } returns flowOf(
            listOf(
                HeartRate(100.5, Clock.System.now() - 2.hours),
                expected,
                HeartRate(150.0, Clock.System.now() - 5.minutes),
            )
        )
        initRepository()

        dashboardRepository.currentHeartRate.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getGraphHeartRates_returnsEmptyList_WhenNoDataAvailable() = runTest {
        every { diaryDao.heartRates } returns flowOf(emptyList())
        initRepository()

        dashboardRepository.graphHeartRates.test {
            assertTrue { awaitItem().isEmpty() }
        }
    }

    /**
     * Given a list with one element exactly 3 hours old and another slightly more recent,
     * the flow shall only return the recent one.
     */
    @Test
    fun getGraphHeartRates_returnsFilteredList_WhenDataAvailable() = runTest {
        val now = Clock.System.now()
        val expected = HeartRate(80.0, now - 2.99.hours)
        every { diaryDao.heartRates } returns flowOf(
            listOf(
                expected,
                HeartRate(190.0, now - 3.hours),
            )
        )
        every { dateTimeRepository.now } returns now
        initRepository()

        dashboardRepository.graphHeartRates.test {
            val actual = awaitItem()
            assertEquals(1, actual.count())
            assertContains(actual, expected)
        }
    }

    /**
     * Given a [DefaultDashboardRepository.graphHeartRates] flow that emits a single value,
     * the resulting flow shall re-emit every minute so that measurements older than 3 hours
     * are always filtered out.
     */
    @Test
    fun getGraphHeartRates_updatesFilter_periodically() = runTest {
        val now = Clock.System.now()
        every { diaryDao.heartRates } returns flowOf(
            listOf(
                HeartRate(80.0, now - (3.hours - 10.seconds)),
                HeartRate(190.0, now - 3.hours),
            )
        )
        every { dateTimeRepository.now } sequentiallyReturns listOf(now, now + 1.minutes)
        initRepository()

        dashboardRepository.graphHeartRates.test {
            awaitItem()
            assertEquals(0, awaitItem().count())
        }
    }

    private fun initRepository() {
        dashboardRepository = DefaultDashboardRepository(diaryDao, dateTimeRepository)
    }
}