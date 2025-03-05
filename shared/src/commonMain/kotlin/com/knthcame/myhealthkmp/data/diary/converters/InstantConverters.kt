package com.knthcame.myhealthkmp.data.diary.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.fromEpochMilliseconds(value)

    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long = instant.toEpochMilliseconds()
}
