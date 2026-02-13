package com.knthcame.myhealthkmp.data.datetime.sources

import kotlinx.datetime.TimeZone
import kotlin.time.Instant

interface DateTimeDao {
    val now: Instant
    val systemTimeZone: TimeZone
}
