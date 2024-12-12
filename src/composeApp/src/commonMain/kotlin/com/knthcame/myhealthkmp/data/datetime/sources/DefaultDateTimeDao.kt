package com.knthcame.myhealthkmp.data.datetime.sources

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

class DefaultDateTimeDao : DateTimeDao {
    override val now: Instant
        get() = Clock.System.now()

    override val systemTimeZone: TimeZone
        get() = TimeZone.currentSystemDefault()
}