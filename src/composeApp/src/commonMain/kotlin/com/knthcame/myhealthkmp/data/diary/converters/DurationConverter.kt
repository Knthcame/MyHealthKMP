package com.knthcame.myhealthkmp.data.diary.converters

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DurationConverter {
    @TypeConverter
    fun fromNanoseconds(value: Long): Duration {
        return value.toDuration(DurationUnit.NANOSECONDS)
    }

    @TypeConverter
    fun durationToNanoseconds(duration: Duration): Long {
        return duration.inWholeNanoseconds
    }
}