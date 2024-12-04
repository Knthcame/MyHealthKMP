package com.knthcame.myhealthkmp.data.diary.repositories

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import com.knthcame.myhealthkmp.data.diary.sources.DiaryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultDiaryRepository(diaryDao: DiaryDao) : DiaryRepository {
    override val currentHeartRate: Flow<HeartRate?> = diaryDao.heartRates.map { heartRates ->
        heartRates.maxByOrNull { rate -> rate.timeStamp }
    }
}