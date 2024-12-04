package com.knthcame.myhealthkmp.data.diary.sources

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

class FakeDiaryDao : DiaryDao {
    override val heartRates: Flow<List<HeartRate>> = flowOf(
        listOf(
            HeartRate(80.0, Clock.System.now())
        )
    )
}