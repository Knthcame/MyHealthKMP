package com.knthcame.myhealthkmp.data.diary.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.knthcame.myhealthkmp.data.diary.model.Activity
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import com.knthcame.myhealthkmp.data.diary.model.Sleep
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    /** Gets all the stored [HeartRate] events, with automatic updates on any change. **/
    @get:Query("SELECT * FROM HeartRatesTable")
    val heartRates: Flow<List<HeartRate>>

    /** Gets all the stored [Sleep] events, with automatic updates on any change. **/
    @get:Query("SELECT * FROM SleepTable")
    val sleeps: Flow<List<Sleep>>

    /** Gets all the stored [Activity] events, with automatic updates on any change. **/
    @get:Query("SELECT * FROM ActivitiesTable")
    val activities: Flow<List<Activity>>

    /** Adds or updates a [Sleep] event. **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(sleep: Sleep)

    /** Adds or updates a [HeartRate] event. **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(heartRate: HeartRate)

    /** Adds or updates a [Activity] event. **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(activity: Activity)

    /** Deletes the specified [HeartRate] event. **/
    @Query("DELETE FROM HeartRatesTable WHERE id = :id")
    suspend fun deleteHearRate(id: Int)

    /** Deletes the specified [Activity] event. **/
    @Query("DELETE FROM ActivitiesTable WHERE id = :id")
    suspend fun deleteActivity(id: Int)

    /** Deletes the specified [Sleep] event. **/
    @Query("DELETE FROM SleepTable WHERE id = :id")
    suspend fun deleteSleep(id: Int)
}