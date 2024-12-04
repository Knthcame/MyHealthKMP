package com.knthcame.myhealthkmp.data.datetime.repositories

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

interface DateTimeRepository {
    val now: Instant
    val localNow: LocalDateTime
    val systemTimeZone: TimeZone
}