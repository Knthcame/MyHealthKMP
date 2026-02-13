package com.knthcame.myhealthkmp.data.datetime.sources

import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultDateTimeDao : DateTimeDao {
    override val now: Instant
        get() = Clock.System.now()

    override val systemTimeZone: TimeZone
        get() = TimeZone.currentSystemDefault()
}
