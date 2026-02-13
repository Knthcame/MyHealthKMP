package com.knthcame.myhealthkmp.data.diary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

/**
 * Contains the beats per minute ([bpm]) and the [timeStamp] when it was measured.
 *
 * @property bpm The measured heart beats per minute.
 * @property timeStamp The date & time when the measurement was done.
 */
@Entity("HeartRatesTable")
data class HeartRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bpm: Double,
    val timeStamp: Instant,
)
