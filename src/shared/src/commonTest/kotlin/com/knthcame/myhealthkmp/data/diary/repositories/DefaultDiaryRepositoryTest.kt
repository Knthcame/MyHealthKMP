package com.knthcame.myhealthkmp.data.diary.repositories

import app.cash.turbine.test
import com.knthcame.myhealthkmp.data.diary.model.Activity
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.data.diary.model.Sleep
import com.knthcame.myhealthkmp.data.diary.repositories.DefaultDiaryRepository
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class DefaultDiaryRepositoryTest {
    private lateinit var diaryDao: DiaryDao
    private lateinit var diaryRepository: DiaryRepository

    @BeforeTest
    fun beforeTest() {
        diaryDao = mock<DiaryDao> {
            every { sleeps } returns flowOf(emptyList())
            every { activities } returns flowOf(emptyList())
        }
    }

    @Test
    fun diaryEvents_returnsEmptyList_whenNoDataIsAvailable() = runTest {
        initRepository()

        diaryRepository.diaryEvents.test {
            assertTrue { awaitItem().isEmpty() }
            awaitComplete()
        }
    }

    /**
     * diaryEvents shall map all [Sleep] events to a [DiaryEvent] with the time stamp,
     * and its duration formatted to a string (in hours).
     * **/
    @Test
    fun diaryEvents_mapsSleepEvents_whenAvailable() = runTest {
        val sleep = Sleep(duration = 3.hours, timeStamp = Clock.System.now())
        every { diaryDao.sleeps } returns flowOf(listOf(sleep))
        initRepository()

        diaryRepository.diaryEvents.test {
            val event = awaitItem().first()
            assertEquals(sleep.duration.toString(DurationUnit.HOURS, 2), event.value)
            assertEquals(sleep.timeStamp, event.timeStamp)
            assertEquals(DiaryEvent.Type.Sleep, event.type)
            awaitComplete()
        }
    }

    /**
     * diaryEvents shall map all [Activity] events to a [DiaryEvent] with the time stamp,
     * and its duration formatted to a string (in minutes).
     * **/
    @Test
    fun diaryEvents_mapsActivityEvents_whenAvailable() = runTest {
        val activity = Activity(duration = 90.minutes, timeStamp = Clock.System.now())
        every { diaryDao.activities } returns flowOf(listOf(activity))
        initRepository()

        diaryRepository.diaryEvents.test {
            val event = awaitItem().first()
            assertEquals(activity.duration.toString(DurationUnit.MINUTES, 0), event.value)
            assertEquals(activity.timeStamp, event.timeStamp)
            assertEquals(DiaryEvent.Type.Activity, event.type)
            awaitComplete()
        }
    }

    /** diaryEvents shall return all events sorted by time stamp, descending (most recent first) **/
    @Test
    fun diaryEvents_sortsEventsByTimestampDescending() = runTest {
        val now = Clock.System.now()
        val event1 = Sleep(duration = 8.hours, timeStamp = now)
        val event2 = Activity(duration = 45.minutes, timeStamp = now - 1.hours)
        val event3 = Activity(duration = 30.minutes, timeStamp = now - 1.days)
        val event4 = Sleep(duration = 6.5.hours, timeStamp = now - 1.days - 4.hours)

        every { diaryDao.sleeps } returns flowOf(listOf(event4, event1))
        every { diaryDao.activities } returns flowOf(listOf(event3, event2))

        initRepository()

        diaryRepository.diaryEvents.test {
            val list = awaitItem()
            assertEquals(event1.timeStamp, list[0].timeStamp)
            assertEquals(event2.timeStamp, list[1].timeStamp)
            assertEquals(event3.timeStamp, list[2].timeStamp)
            assertEquals(event4.timeStamp, list[3].timeStamp)
            awaitComplete()
        }
    }

    @Test
    fun delete_deletesActivityById_whenEventTypeIsActivity() = runTest {
        val id = 4
        initRepository()

        diaryRepository.delete(DiaryEvent.Type.Activity, id)

        verifySuspend { diaryDao.deleteActivity(id) }
    }

    @Test
    fun delete_deletesSleepById_whenEventTypeIsSleep() = runTest {
        val id = 9
        initRepository()

        diaryRepository.delete(DiaryEvent.Type.Sleep, id)

        verifySuspend { diaryDao.deleteSleep(id) }
    }

    @Test
    fun save_addsActivity_whenEventTypeIsActivity() = runTest {
        val timestamp = Clock.System.now() - 5.hours
        val durationInMinutes = 30
        val event = DiaryEvent(
            timeStamp = timestamp,
            value = durationInMinutes.toString(),
            type = DiaryEvent.Type.Activity,
        )
        initRepository()

        diaryRepository.save(event)

        verifySuspend {
            diaryDao.addOrUpdate(
                Activity(
                    id = 0,
                    duration = durationInMinutes.minutes,
                    timeStamp = timestamp,
                )
            )
        }
    }

    @Test
    fun save_addsSleep_whenEventTypeIsSleep() = runTest {
        val timestamp = Clock.System.now() - 16.hours
        val durationInHours = 8.5
        val event = DiaryEvent(
            timeStamp = timestamp,
            value = durationInHours.toString(),
            type = DiaryEvent.Type.Sleep,
        )
        initRepository()

        diaryRepository.save(event)

        verifySuspend {
            diaryDao.addOrUpdate(
                Sleep(
                    id = 0,
                    duration = durationInHours.hours,
                    timeStamp = timestamp,
                )
            )
        }
    }

    private fun initRepository() {
        diaryRepository = DefaultDiaryRepository(diaryDao)
    }
}