package com.knthcame.myhealthkmp.data.datetime.sources

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

interface DateTimeDao {
    val now: Instant
    val systemTimeZone: TimeZone
}