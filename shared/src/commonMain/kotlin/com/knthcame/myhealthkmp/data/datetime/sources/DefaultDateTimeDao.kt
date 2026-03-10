package com.knthcame.myhealthkmp.data.datetime.sources

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.TimeZone

class DefaultDateTimeDao : DateTimeDao {
    override val now: Instant
        get() = Clock.System.now()

    override val systemTimeZone: TimeZone
        get() = TimeZone.currentSystemDefault()
}
