package com.knthcame.myhealthkmp.data.dashboard.repositories

import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class DefaultDashboardRepository(
    diaryDao: DiaryDao,
    dateTimeRepository: DateTimeRepository,
) : DashboardRepository {
    private val graphInterval = 3.hours
    private val timeTicker =
        flow {
            while (true) {
                emit(Unit)
                delay(1.minutes)
            }
        }

    override val currentHeartRate: Flow<HeartRate?> =
        diaryDao.heartRates.map { heartRates ->
            heartRates.maxByOrNull { rate -> rate.timeStamp }
        }

    override val graphHeartRates: Flow<List<HeartRate>> =
        diaryDao.heartRates.combine(timeTicker) { heartRates, _ ->
            val referenceDateTime = dateTimeRepository.now - graphInterval
            heartRates.filter { item -> item.timeStamp > referenceDateTime }
        }
}
