package com.knthcame.myhealthkmp.data.diary.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }

    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }
}