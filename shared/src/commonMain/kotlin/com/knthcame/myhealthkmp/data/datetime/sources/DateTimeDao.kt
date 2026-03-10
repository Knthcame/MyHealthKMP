package com.knthcame.myhealthkmp.data.datetime.sources

import kotlin.time.Instant
import kotlinx.datetime.TimeZone

interface DateTimeDao {
    val now: Instant
    val systemTimeZone: TimeZone
}
