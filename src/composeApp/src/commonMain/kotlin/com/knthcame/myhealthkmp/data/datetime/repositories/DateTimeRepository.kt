package com.knthcame.myhealthkmp.data.datetime.repositories

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

interface DateTimeRepository {
    val now: Instant
    val localNow: LocalDateTime
    val systemTimeZone: TimeZone
}