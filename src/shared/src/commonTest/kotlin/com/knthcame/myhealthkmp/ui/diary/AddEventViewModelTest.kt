package com.knthcame.myhealthkmp.ui.diary

import app.cash.turbine.test
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent.Type.Activity
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent.Type.Sleep
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import com.knthcame.myhealthkmp.utils.testViewModelScope
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddEventViewModelTest {
    private val localDateTime = LocalDateTime(2026, 2, 13, 10, 30)
    private val timeZone = TimeZone.currentSystemDefault()

    private val diaryRepository: DiaryRepository = mock()
    private val dateTimeRepository: DateTimeRepository = mock {
        every { localNow } returns localDateTime
        every { systemTimeZone } returns timeZone
    }
    private val sut by lazy {
        AddEventViewModel(
            diaryRepository = diaryRepository,
            dateTimeRepository = dateTimeRepository,
            viewModelScope = testViewModelScope,
        )
    }

    @Test
    fun uiState_returnsCurrentDateTime_onInitialState() = runTest {
        sut.uiState.test {
            val actual = awaitItem()

            assertEquals(localDateTime.date, actual.entryDate)
            assertEquals(localDateTime.time, actual.entryTime)
            assertEquals(localDateTime.date, actual.maxSelectableDate)
            assertEquals(localDateTime.time, actual.maxSelectableTime)
        }
    }

    @Test
    fun editEventDate_updatesState_onCall() = runTest {
        sut.uiState.test {
            awaitItem()
            sut.editEventDate(1769939278000)
            assertEquals(
                expected = LocalDate(2026, 2, 1),
                actual = awaitItem().entryDate,
            )
        }
    }

    @Test
    fun editEventTime_updatesState_onCall() = runTest {
        sut.uiState.test {
            awaitItem()
            sut.editEventTime(5, 23)
            assertEquals(
                expected = LocalTime(5, 23),
                actual = awaitItem().entryTime,
            )
        }
    }

    @Test
    fun editEventValue_updatesState_onCall() = runTest {
        sut.uiState.test {
            awaitItem()
            sut.editValue("test value")
            assertEquals(
                expected = "test value",
                actual = awaitItem().value,
            )
        }
    }

    @Test
    fun editEventType_updatesState_onCall() = runTest {
        sut.uiState.test {
            assertEquals(expected = Activity, actual = awaitItem().entryType)
            sut.editEventType(Sleep)
            assertEquals(
                expected = Sleep,
                actual = awaitItem().entryType,
            )
        }
    }

    @Test
    fun saveEvent_savesEventInRepository_onCall() = runTest {
        sut.uiState.test {
            val state = awaitItem()

            sut.saveEvent()

            verifySuspend {
                diaryRepository.save(
                    event = DiaryEvent(
                        id = 0,
                        value = state.value,
                        timeStamp = localDateTime.toInstant(timeZone),
                        type = state.entryType,
                    )
                )
            }
            assertTrue(awaitItem().isSaved)
        }
    }
}