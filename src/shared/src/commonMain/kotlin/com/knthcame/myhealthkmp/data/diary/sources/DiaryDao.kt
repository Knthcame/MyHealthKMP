package com.knthcame.myhealthkmp.data.diary.sources

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.flow.Flow

interface DiaryDao {
    val heartRates: Flow<List<HeartRate>>
}