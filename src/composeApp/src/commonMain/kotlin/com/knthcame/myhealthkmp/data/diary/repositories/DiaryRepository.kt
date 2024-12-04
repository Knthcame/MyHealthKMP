package com.knthcame.myhealthkmp.data.diary.repositories

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    val currentHeartRate: Flow<HeartRate?>
}