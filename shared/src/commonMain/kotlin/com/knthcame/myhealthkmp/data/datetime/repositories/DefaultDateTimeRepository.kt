package com.knthcame.myhealthkmp.data.datetime.repositories

import com.knthcame.myhealthkmp.data.datetime.sources.DateTimeDao
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class DefaultDateTimeRepository(
    private val dateTimeDao: DateTimeDao,
) : DateTimeRepository {
    override val now: Instant
        get() = dateTimeDao.now

    override val localNow: LocalDateTime
        get() = now.toLocalDateTime(dateTimeDao.systemTimeZone)

    override val systemTimeZone: TimeZone
        get() = dateTimeDao.systemTimeZone
}
