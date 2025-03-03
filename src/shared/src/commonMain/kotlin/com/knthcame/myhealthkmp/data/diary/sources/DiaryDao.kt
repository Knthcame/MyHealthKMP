package com.knthcame.myhealthkmp.data.diary.sources

import androidx.room.Dao
import androidx.room.Query
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @get:Query("SELECT * FROM HeartRatesTable")
    val heartRates: Flow<List<HeartRate>>
}