package com.knthcame.myhealthkmp.data.diary.repositories

import com.knthcame.myhealthkmp.data.diary.model.Activity
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.data.diary.model.Sleep
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class DefaultDiaryRepository(
    private val diaryDao: DiaryDao,
) : DiaryRepository {
    override val diaryEvents: Flow<List<DiaryEvent>> =
        combine(diaryDao.sleeps, diaryDao.activities) { sleeps, activities ->
            val sleepEvents = sleeps.map { sleep ->
                DiaryEvent(
                    id = sleep.id,
                    value = sleep.duration.toString(DurationUnit.HOURS, 2),
                    timeStamp = sleep.timeStamp,
                    type = DiaryEvent.Type.Sleep,
                )
            }
            val activityEvents = activities.map { activity ->
                DiaryEvent(
                    id = activity.id,
                    value = activity.duration.toString(DurationUnit.MINUTES),
                    timeStamp = activity.timeStamp,
                    type = DiaryEvent.Type.Activity,
                )
            }

            return@combine (sleepEvents + activityEvents).sortedByDescending { event -> event.timeStamp }
        }

    override suspend fun delete(type: DiaryEvent.Type, id: Int) {
        when (type) {
            DiaryEvent.Type.Activity -> diaryDao.deleteActivity(id)
            DiaryEvent.Type.Sleep -> diaryDao.deleteSleep(id)
        }
    }

    override suspend fun save(event: DiaryEvent) {
        when (event.type) {
            DiaryEvent.Type.Activity -> {
                val activity = Activity(
                    duration = event.value.toDouble().minutes,
                    timeStamp = event.timeStamp,
                )
                diaryDao.addOrUpdate(activity)
            }

            DiaryEvent.Type.Sleep -> {
                val sleep = Sleep(
                    duration = event.value.toDouble().hours,
                    timeStamp = event.timeStamp,
                )
                diaryDao.addOrUpdate(sleep)
            }
        }
    }
}