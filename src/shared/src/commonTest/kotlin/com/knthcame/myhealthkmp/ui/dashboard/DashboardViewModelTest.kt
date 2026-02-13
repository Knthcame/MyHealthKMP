package com.knthcame.myhealthkmp.ui.dashboard

import app.cash.turbine.test
import com.knthcame.myhealthkmp.data.dashboard.repositories.DashboardRepository
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import com.knthcame.myhealthkmp.ui.common.DateTimeStyle
import com.knthcame.myhealthkmp.ui.common.formatWithCurrentLocale
import com.knthcame.myhealthkmp.utils.testViewModelScope
import dev.mokkery.answering.returns
import dev.mokkery.answering.returnsBy
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class DashboardViewModelTest {
    private lateinit var viewModel: DashboardViewModel

    private val dashboardRepository = mock<DashboardRepository> {
        every { currentHeartRate } returns emptyFlow()
        every { graphHeartRates } returns emptyFlow()
    }
    private val dateTimeRepository = mock<DateTimeRepository> {
        every { now } returnsBy { Clock.System.now() }
        every { localNow } returnsBy {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }
        every { systemTimeZone } returns TimeZone.currentSystemDefault()
    }

    @Test
    fun heartRateState_returnsMissing_asDefaultValue() {
        initViewmodel()

        val actual = viewModel.heartRateState.value

        assertEquals(HeartRateUiState.Missing, actual)
    }

    @Test
    fun heartRateState_returnsMissing_whenCurrentHeartRateIsNull() = runTest {
        every { dashboardRepository.currentHeartRate } returns flowOf(null)
        every { dashboardRepository.graphHeartRates } returns flowOf(
            listOf(
                HeartRate(bpm = 80.0, timeStamp = Clock.System.now())
            )
        )

        initViewmodel()

        viewModel.heartRateState.test {
            val actual = awaitItem()

            assertEquals(HeartRateUiState.Missing, actual)
        }
    }

    @Test
    fun heartRateState_returnsCurrentValue_whenCurrentHearRateAvailable() = runTest {
        val expected = HeartRate(bpm = 74.8, timeStamp = Clock.System.now())
        every { dashboardRepository.currentHeartRate } returns flowOf(expected)
        every { dashboardRepository.graphHeartRates } returns flowOf(listOf(expected))
        initViewmodel()

        viewModel.heartRateState.test {
            val actual = awaitItem()

            assertIs<HeartRateUiState.Available>(actual)
            assertEquals(expected.bpm, actual.value)
        }
    }

    @Test
    fun heartRateState_returnsFormattedTime_whenCurrentHeartRateIsFromToday() = runTest {
        val expected = HeartRate(bpm = 74.8, timeStamp = Clock.System.now())
        every { dashboardRepository.currentHeartRate } returns flowOf(expected)
        every { dashboardRepository.graphHeartRates } returns flowOf(listOf(expected))
        initViewmodel()

        val localExpectedTime =
            expected.timeStamp.toLocalDateTime(TimeZone.currentSystemDefault()).time
        viewModel.heartRateState.test {
            val actual = awaitItem()

            assertIs<HeartRateUiState.Available>(actual)
            assertEquals(localExpectedTime.formatWithCurrentLocale(), actual.timeStamp)
        }
    }

    @Test
    fun heartRateState_returnsFormattedDateTime_whenCurrentHeartRateIsNotFromToday() = runTest {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = LocalDateTime(2024, 12, 11, 0, 0)
        val utcDateTime = startOfDay.toInstant(timeZone)
        every { dateTimeRepository.now } returns utcDateTime
        every { dateTimeRepository.localNow } returns startOfDay
        every { dateTimeRepository.systemTimeZone } returns timeZone

        val expected = HeartRate(bpm = 74.8, timeStamp = utcDateTime - 1.seconds)
        every { dashboardRepository.currentHeartRate } returns flowOf(expected)
        every { dashboardRepository.graphHeartRates } returns flowOf(listOf(expected))

        initViewmodel()

        val localExpectedDateTime = expected.timeStamp.toLocalDateTime(timeZone)
        viewModel.heartRateState.test {
            val actual = awaitItem()

            assertIs<HeartRateUiState.Available>(actual)
            assertEquals(
                localExpectedDateTime.formatWithCurrentLocale(DateTimeStyle.Short),
                actual.timeStamp,
            )
        }
    }

    @Test
    fun heartRateState_ordersGraphList_whenAvailable() = runTest {
        val unorderedList = listOf(
            HeartRate(bpm = 80.2, timeStamp = Clock.System.now() - 4.hours),
            HeartRate(bpm = 94.1, timeStamp = Clock.System.now())
        )
        val orderedList = unorderedList.sortedBy { item -> item.timeStamp }

        every { dashboardRepository.currentHeartRate } returns flowOf(orderedList.last())
        every { dashboardRepository.graphHeartRates } returns flowOf(unorderedList)
        initViewmodel()

        viewModel.heartRateState.test {
            val actual = awaitItem()

            assertIs<HeartRateUiState.Available>(actual)
            assertContentEquals(orderedList, actual.graphValues)
        }
    }

    private fun initViewmodel() {
        viewModel = DashboardViewModel(
            dashboardRepository = dashboardRepository,
            dateTimeRepository = dateTimeRepository,
            viewModelScope = testViewModelScope,
        )
    }
}