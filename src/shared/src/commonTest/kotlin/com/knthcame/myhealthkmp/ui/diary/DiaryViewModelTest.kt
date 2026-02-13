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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class DiaryViewModelTest {
    private val now = Clock.System.now()
    private val timeZone = TimeZone.currentSystemDefault()
    private val events = listOf(
        DiaryEvent(id = 1, value = "30 min", timeStamp = now, type = Activity),
        DiaryEvent(id = 2, value = "6 h", timeStamp = now, type = Sleep),
        DiaryEvent(id = 3, value = "8 h", timeStamp = now.minus(1.days), type = Sleep),
        DiaryEvent(id = 4, value = "7 h", timeStamp = now.minus(3.days), type = Sleep),
    )

    private val diaryRepository: DiaryRepository = mock {
        every { diaryEvents } returns flowOf(events)
    }
    private val dateTimeRepository: DateTimeRepository = mock {
        every { systemTimeZone } returns timeZone
    }

    private val sut by lazy {
        DiaryViewModel(
            diaryRepository = diaryRepository,
            dateTimeRepository = dateTimeRepository,
            viewModelScope = testViewModelScope,
        )
    }

    @Test
    fun groupedDiaryEvents_returnsDiaryEvents_groupedByDate() = runTest {
        sut.groupedDiaryEvents.test {
            val actual = awaitItem()

            assertEquals(
                expected = mapOf(
                    now.toLocalDateTime(timeZone).date to listOf(
                        DiaryUIEvent(
                            localDate = now.toLocalDateTime(timeZone).date,
                            localTime = now.toLocalDateTime(timeZone).time,
                            value = "30 min",
                            eventId = 1,
                            eventType = Activity,
                        ),
                        DiaryUIEvent(
                            localDate = now.toLocalDateTime(timeZone).date,
                            localTime = now.toLocalDateTime(timeZone).time,
                            value = "6 h",
                            eventId = 2,
                            eventType = Sleep,
                        ),
                    ),
                    now.minus(1.days).toLocalDateTime(timeZone).date to listOf(
                        DiaryUIEvent(
                            localDate = now.minus(1.days).toLocalDateTime(timeZone).date,
                            localTime = now.minus(1.days).toLocalDateTime(timeZone).time,
                            value = "8 h",
                            eventId = 3,
                            eventType = Sleep,
                        ),
                    ),
                    now.minus(3.days).toLocalDateTime(timeZone).date to listOf(
                        DiaryUIEvent(
                            localDate = now.minus(3.days).toLocalDateTime(timeZone).date,
                            localTime = now.minus(3.days).toLocalDateTime(timeZone).time,
                            value = "7 h",
                            eventId = 4,
                            eventType = Sleep,
                        ),
                    ),
                ),
                actual = actual
            )
        }
    }

    @Test
    fun delete_deletesEventFromRepository_onCall() = runTest {
        val event = DiaryUIEvent(
            localDate = now.minus(3.days).toLocalDateTime(timeZone).date,
            localTime = now.minus(3.days).toLocalDateTime(timeZone).time,
            value = "7 h",
            eventId = 4,
            eventType = Sleep,
        )

        sut.delete(event)

        verifySuspend { diaryRepository.delete(event.eventType, event.eventId) }
    }
}