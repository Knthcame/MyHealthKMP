package com.knthcame.myhealthkmp.data.diary.model

import kotlin.time.Instant

/**
 * Contains the beats per minute ([bpm]) and the [timeStamp] when it was measured
 *
 * @property bpm The measured heart beats per minute.
 * @property timeStamp The date & time when the measurement was done.
 */
data class HeartRate(
    val bpm: Double,
    val timeStamp: Instant,
)
